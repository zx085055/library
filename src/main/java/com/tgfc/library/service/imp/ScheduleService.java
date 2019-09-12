package com.tgfc.library.service.imp;

import com.tgfc.library.entity.Schedule;
import com.tgfc.library.enums.JobLastExecuteEnum;
import com.tgfc.library.enums.JobTypeEnum;
import com.tgfc.library.enums.ScheduleStatus;
import com.tgfc.library.repository.IEmployeeRepository;
import com.tgfc.library.repository.IScheduleRepository;
import com.tgfc.library.request.SchedulePageRequset;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.response.SchedulePageResponse;
import com.tgfc.library.schedule.scheduler.MyScheduler;
import com.tgfc.library.schedule.trigger.OneDayOneTimeTrigger;
import com.tgfc.library.service.IScheduleService;
import com.tgfc.library.util.ContextUtil;
import org.quartz.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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


    /**
     * 動態查詢各參數(排程名稱，起始時間，結束時間)
     */
    @Override
    public BaseResponse list(SchedulePageRequset model) throws ParseException {
        BaseResponse response = new BaseResponse();
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
                .setMaxResults((model.getPageNumber() - 1) * model.getPageSize() + model.getPageSize())
                .getResultList();
        response.setData(scheduleListToResponseList(list));
        response.setMessage("查詢成功");
        response.setStatus(true);
        return response;
    }

    private SchedulePageRequset initKeyword(SchedulePageRequset model) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = sdf.parse("1970-01-01");
            endDate = sdf.parse("2099-01-01");
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
                    BeanUtils.copyProperties(schedule,schedulePageResponse);
                    return schedulePageResponse;
                }).collect(Collectors.toList());
    }

    /**
     * 新增排程
     */
    @Override
    public BaseResponse create(SchedulePageRequset model) {
        BaseResponse response = new BaseResponse();
        Schedule schedule = saveSchedule(model);
        model = getAndSetIdWithModelAndPo(model, schedule);
        try {
            JobDetail job = getJob(model);
            CronTrigger trigger = oneDayOneTimeTrigger.getTrigger(model);
            myScheduler.addJob(job, trigger);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setScheduleStatus(model);
        response.setData(true);
        response.setMessage("新增排程no." + model.getId() + "成功");
        response.setStatus(true);
        return response;
    }

    private void setScheduleStatus(SchedulePageRequset model) {
        if (ScheduleStatus.DISABLE.getCode().equals(model.getScheduleStatus())) {
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
        return model;
    }

    private Schedule saveSchedule(SchedulePageRequset model) {
        Schedule schedule = modelToPo(model);
        schedule.setStatus(ScheduleStatus.UNDONE.getCode());
        schedule.setGroup(model.getName() + model.getId());
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
        JobKey jobKey = new JobKey(model.getName(), model.getName() + model.getId());
        JobDetail job = JobBuilder.newJob(JobTypeEnum.codeToClass(model.getType()))
                .withIdentity(jobKey)
                .usingJobData("id", model.getId())
                .build();
        return job;
    }

    /**
     * 改變排程狀態 ( 啟用 <---> 禁用 )
     */
    @Override
    public BaseResponse changeStatus(int id) {
        BaseResponse response = new BaseResponse();
        Schedule schedule = scheduleRepository.getById(id);
        if (schedule == null) {
            response.setMessage("查無此排程");
            response.setStatus(false);
            return response;
        }
        JobKey jobKey = new JobKey(schedule.getName(), schedule.getName() + schedule.getId());
        if (ScheduleStatus.ENABLE.getCode().equals(schedule.getStatus())) {
            myScheduler.pauseJob(jobKey);
            scheduleRepository.pauseJob(id);
            response.setMessage("狀態改變成功，排程" + id + "由" + ScheduleStatus.ENABLE.getTrans() + "變為" + ScheduleStatus.DISABLE.getTrans());
        } else if (ScheduleStatus.DISABLE.getCode().equals(schedule.getStatus())) {
            myScheduler.resumeJob(jobKey);
            scheduleRepository.resumeJob(id);
            response.setMessage("狀態改變成功，排程" + id + "由" + ScheduleStatus.DISABLE.getTrans() + "變為" + ScheduleStatus.ENABLE.getTrans());
        }
        response.setData(true);
        response.setStatus(true);
        return response;
    }

    @Override
    public BaseResponse deleteAllJobs() {
        BaseResponse response = new BaseResponse();
        response.setData(myScheduler.deleteAllJobs());
        scheduleRepository.deleteAll();
        response.setMessage("全部刪除完成");
        response.setStatus(true);
        return response;
    }

    @Override
    public BaseResponse pauseAll() {
        BaseResponse response = new BaseResponse();
        try {
            myScheduler.pauseAll();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        scheduleRepository.pauseAll();
        response.setData(true);
        response.setMessage("全部排程暫停");
        response.setStatus(true);
        return response;
    }

    @Override
    public BaseResponse resumeAll() {
        BaseResponse response = new BaseResponse();
        try {
            myScheduler.resumeAll();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        scheduleRepository.resumeAll();
        response.setData(true);
        response.setMessage("全部暫停排程恢復");
        response.setStatus(true);
        return response;
    }

    @Override
    public BaseResponse delete(int id) {
        BaseResponse response = new BaseResponse();
        Schedule schedule = scheduleRepository.getById(id);
        if (schedule == null) {
            response.setMessage("查無此排程");
            response.setStatus(false);
            return response;
        }
        JobKey jobKey = new JobKey(schedule.getName(), schedule.getName() + schedule.getId());
        try {
            response.setData(myScheduler.deleteJob(jobKey));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        scheduleRepository.deleteById(id);
        response.setMessage("刪除指定排程成功");
        response.setStatus(true);
        return response;
    }

    @Override
    public BaseResponse edit(SchedulePageRequset model) {
        BaseResponse response = new BaseResponse();
        if (scheduleRepository.getById(model.getId()) == null) {
            response.setMessage("查無此排程");
            response.setStatus(false);
            return response;
        }
        model.setIsEdit(true);
        model.setLastExecute(scheduleRepository.getLastExecute(model.getId()));
        scheduleRepository.deleteById(model.getId());
        response.setData(this.create(model).getStatus());
        response.setMessage("修改成功");
        response.setStatus(true);
        return response;
    }
}
