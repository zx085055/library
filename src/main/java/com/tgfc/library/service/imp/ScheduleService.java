package com.tgfc.library.service.imp;

import com.tgfc.library.entity.Schedule;
import com.tgfc.library.enums.JobLastExecuteEnum;
import com.tgfc.library.enums.JobTypeEnum;
import com.tgfc.library.enums.ScheduleEnum;
import com.tgfc.library.enums.ScheduleStatusEnum;
import com.tgfc.library.repository.IEmployeeRepository;
import com.tgfc.library.repository.IScheduleRepository;
import com.tgfc.library.request.SchedulePageRequset;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.response.SchedulePageResponse;
import com.tgfc.library.schedule.job.NoticeJob;
import com.tgfc.library.schedule.scheduler.MyScheduler;
import com.tgfc.library.schedule.trigger.OneDayOneTimeTrigger;
import com.tgfc.library.service.IScheduleService;
import com.tgfc.library.util.ContextUtil;
import org.quartz.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ScheduleService implements IScheduleService {

    @Autowired
    IScheduleRepository scheduleRepository;

    @Autowired
    private EntityManagerFactory emf;

    @Autowired
    OneDayOneTimeTrigger oneDayOneTimeTrigger;

    @Autowired
    MyScheduler myScheduler;

    @Autowired
    IEmployeeRepository employeeRepository;

    private BaseResponse.Builder builder;


    /**
     * 動態查詢各參數(排程名稱，起始時間，結束時間)
     */
    @Override
    public BaseResponse list(SchedulePageRequset model) {
        builder = new BaseResponse.Builder();
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction etx = entityManager.getTransaction();
        etx.begin();
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Schedule> query = criteriaBuilder.createQuery(Schedule.class);
        Root<Schedule> rootEntity = query.from(Schedule.class);

        model = initKeyword(model);

        Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(rootEntity.get("name"), model.getName()),
                criteriaBuilder.greaterThanOrEqualTo(rootEntity.get("startTime"), model.getStartTime()),
                criteriaBuilder.lessThanOrEqualTo(rootEntity.get("endTime"), model.getEndTime()));

        query = query.select(rootEntity).where(predicate);
        List<Schedule> list = entityManager.createQuery(query)
                .setFirstResult((model.getPageNumber() - 1) * model.getPageSize())
                .setMaxResults(model.getPageSize())
                .getResultList();

        int totalCount = entityManager.createQuery(query).getResultList().size();

        etx.commit();
        entityManager.close();

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("totalCount", totalCount);
        resultMap.put("results", scheduleListToResponseList(list));

        return builder.content(resultMap).message("查詢成功").status(true).build();
    }

    private SchedulePageRequset initKeyword(SchedulePageRequset model) {
        model.setPageNumber(model.getPageNumber() < 1 ? 1 : model.getPageNumber());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = sdf.parse("0000-01-01");
            endDate = sdf.parse("3099-01-01");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        model.setName(model.getName() == null ? "%" : "%" + model.getName() + "%");
        model.setStartTime(model.getStartTime() == null ? startDate : model.getStartTime());
        model.setEndTime(model.getEndTime() == null ? endDate : model.getEndTime());

        return model;
    }

    /**
     * schedule轉為SchedulePageResponse
     */
    private List<SchedulePageResponse> scheduleListToResponseList(List<Schedule> list) {
        return list.stream()
                .map(schedule -> {
                    SchedulePageResponse schedulePageResponse = new SchedulePageResponse();
                    BeanUtils.copyProperties(schedule, schedulePageResponse);
                    return schedulePageResponse;
                }).collect(Collectors.toList());
    }

    /**
     * 新增排程
     */
    @Override
    public BaseResponse create(SchedulePageRequset model) {
        builder = new BaseResponse.Builder();
        try {
        Schedule schedule = saveSchedule(model);
        model = getAndSetIdWithModelAndPo(model, schedule);
            JobDetail job = getJob(model);
            CronTrigger trigger = oneDayOneTimeTrigger.getTrigger(model);
            myScheduler.addJob(job, trigger);
        } catch (Exception e) {
            e.printStackTrace();
            return builder.message("排程參數異常").status(false).build();
        }
        setScheduleStatus(model);
        return builder.content(true).message("新增排程成功").status(true).build();
    }

    private void setScheduleStatus(SchedulePageRequset model) {
        if (ScheduleStatusEnum.DISABLE.getCode().equals(model.getScheduleStatus())) {
            this.changeStatus(model.getId());
        }
        scheduleRepository.setStatus(model.getId(), model.getScheduleStatus());
    }

    private SchedulePageRequset getAndSetIdWithModelAndPo(SchedulePageRequset model, Schedule schedule) {
        if (model.getIsEdit() != null && model.getIsEdit()) {
            scheduleRepository.setId(schedule.getId(), model.getId());
        } else {
            model.setId(schedule.getId());
        }
        scheduleRepository.setGroup(model.getId(), ScheduleEnum.GROUP.getCode() + model.getId());
        return model;
    }

    private Schedule saveSchedule(SchedulePageRequset model) {
        Schedule schedule = modelToPo(model);
        schedule.setStatus(ScheduleStatusEnum.UNDONE.getCode());
        if (model.getIsEdit() != null && model.getIsEdit()) {
            schedule.setLastExecute(model.getLastExecute());
        } else {
            schedule.setLastExecute(JobLastExecuteEnum.UNDONE.getCode());
        }
        schedule.setEmployee(employeeRepository.findById(ContextUtil.getAccount()).get());
        return scheduleRepository.save(schedule);
    }

    private Schedule modelToPo(SchedulePageRequset model) {
        Schedule schedule = new Schedule();
        schedule.setName(model.getName());
        schedule.setType(model.getType());
        schedule.setNoticeTime(model.getNoticeTime());
        schedule.setStartTime(model.getStartTime());
        schedule.setEndTime(model.getEndTime());
        schedule.setStatus(model.getScheduleStatus());
        schedule.setJobName(JobTypeEnum.codeToTrans(model.getType()));
        return schedule;
    }

    private SchedulePageRequset poToModel(Schedule schedule) {
        SchedulePageRequset model = new SchedulePageRequset();
        model.setName(schedule.getName());
        model.setType(schedule.getType());
        model.setNoticeTime(schedule.getNoticeTime());
        model.setStartTime(schedule.getStartTime());
        model.setEndTime(schedule.getEndTime());
        model.setId(schedule.getId());
        model.setScheduleStatus(schedule.getStatus());
        return model;
    }


    private JobDetail getJob(SchedulePageRequset model) {
        JobKey jobKey = new JobKey(ScheduleEnum.JOB.getCode() + model.getId(), ScheduleEnum.GROUP.getCode() + model.getId());
        JobDetail job = JobBuilder.newJob(NoticeJob.class)
                .withIdentity(jobKey)
                .usingJobData("id", model.getId())
                .usingJobData("jobType", model.getType())
                .build();
        return job;
    }

    /**
     * 改變排程狀態 ( 啟用 <---> 禁用 )
     */
    @Override
    public BaseResponse changeStatus(int id) {
        builder = new BaseResponse.Builder();
        Schedule schedule = scheduleRepository.getById(id);
        if (schedule == null) {
            return builder.message("查無此排程").status(false).build();
        }
        JobKey jobKey = new JobKey(ScheduleEnum.JOB.getCode() + schedule.getId(), ScheduleEnum.GROUP.getCode() + schedule.getId());
        try {
            if (ScheduleStatusEnum.ENABLE.getCode().equals(schedule.getStatus())) {
                myScheduler.pauseJob(jobKey);
                scheduleRepository.pauseJob(id);
                builder.message("狀態改變成功，排程由" + ScheduleStatusEnum.ENABLE.getTrans() + "變為" + ScheduleStatusEnum.DISABLE.getTrans());
            } else if (ScheduleStatusEnum.DISABLE.getCode().equals(schedule.getStatus())) {
                myScheduler.resumeJob(jobKey);
                scheduleRepository.resumeJob(id);
                builder.message("狀態改變成功，排程由" + ScheduleStatusEnum.DISABLE.getTrans() + "變為" + ScheduleStatusEnum.ENABLE.getTrans());
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return builder.status(true).content(true).build();
    }

    @Override
    public BaseResponse deleteAllJobs() {
        builder = new BaseResponse.Builder();
        try {
            builder.content(myScheduler.deleteAllJobs());
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        scheduleRepository.deleteAll();
        return builder.message("全部刪除完成").status(true).build();
    }

    @Override
    public BaseResponse pauseAll() {
        builder = new BaseResponse.Builder();
        try {
            myScheduler.pauseAll();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        scheduleRepository.pauseAll();
        return builder.content(true).status(true).message("全部排程暫停").build();
    }

    @Override
    public BaseResponse resumeAll() {
        builder = new BaseResponse.Builder();
        try {
            myScheduler.resumeAll();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        scheduleRepository.resumeAll();
//        response.setData(true);
//        response.setMessage("全部暫停排程恢復");
//        response.setStatus(true);
//        return response;
        return builder.content(true).status(true).message("全部暫停排程恢復").build();
    }

    @Override
    public BaseResponse delete(int id) {
        builder = new BaseResponse.Builder();
        Schedule schedule = scheduleRepository.getById(id);
        if (schedule == null) {
           return builder.message("查無此排程").status(false).build();
        }
        JobKey jobKey = new JobKey(ScheduleEnum.JOB.getCode() + schedule.getId(), ScheduleEnum.GROUP.getCode() + schedule.getId());
        try {
            builder.content(myScheduler.deleteJob(jobKey));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        scheduleRepository.deleteById(id);
        return builder.message("刪除指定排程成功").status(true).build();
    }

    @Override
    public BaseResponse edit(SchedulePageRequset model) {
        builder = new BaseResponse.Builder();
        if (scheduleRepository.getById(model.getId()) == null) {
            return builder.message("查無此排程").status(false).build();
        }
        model.setIsEdit(true);
        model.setLastExecute(scheduleRepository.getLastExecute(model.getId()));
        this.delete(model.getId());
        return builder.content(this.create(model).getStatus()).message("修改成功").status(true).build();
    }

}
