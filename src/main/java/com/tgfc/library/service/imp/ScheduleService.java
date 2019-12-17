package com.tgfc.library.service.imp;

import com.tgfc.library.entity.Schedule;
import com.tgfc.library.enums.JobLastExecuteEnum;
import com.tgfc.library.enums.JobTypeEnum;
import com.tgfc.library.enums.ScheduleEnum;
import com.tgfc.library.enums.ScheduleStatusEnum;
import com.tgfc.library.repository.IEmployeeRepository;
import com.tgfc.library.repository.IScheduleRepository;
import com.tgfc.library.request.SchedulePageRequest;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.response.SchedulePageResponse;
import com.tgfc.library.schedule.job.AbstractJob;
import com.tgfc.library.schedule.scheduler.MyScheduler;
import com.tgfc.library.schedule.trigger.OneDayOneTimeTrigger;
import com.tgfc.library.service.IScheduleService;
import com.tgfc.library.util.ContextUtil;
import com.tgfc.library.util.MessageUtil;
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

    @Override
    public BaseResponse list(SchedulePageRequest model) {
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

        Map<String, Object> resultMap = new HashMap<>(16);
        resultMap.put("totalCount", totalCount);
        resultMap.put("results", scheduleListToResponseList(list));

        return builder.content(resultMap).message(MessageUtil.getMessage("schedule.searchSuccess")).status(true).build();
    }

    private SchedulePageRequest initKeyword(SchedulePageRequest model) {
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

    private List<SchedulePageResponse> scheduleListToResponseList(List<Schedule> list) {
        return list.stream()
                .map(schedule -> {
                    SchedulePageResponse schedulePageResponse = new SchedulePageResponse();
                    BeanUtils.copyProperties(schedule, schedulePageResponse);
                    return schedulePageResponse;
                }).collect(Collectors.toList());
    }


    @Override
    public BaseResponse create(SchedulePageRequest model) {
        builder = new BaseResponse.Builder();
        try {
            Schedule schedule = saveSchedule(model);
            model = getAndSetIdWithModelAndPo(model, schedule);
            JobDetail job = getJob(model);
            CronTrigger trigger = oneDayOneTimeTrigger.getTrigger(model);
            myScheduler.addJob(job, trigger);
        } catch (Exception e) {
            e.printStackTrace();
            return builder.message(MessageUtil.getMessage("schedule.parameterError")).status(false).build();
        }
        setScheduleStatus(model);
        return builder.content(true).message(MessageUtil.getMessage("schedule.insertSuccess")).status(true).build();
    }

    private void setScheduleStatus(SchedulePageRequest model) {
        if (ScheduleStatusEnum.DISABLE.getCode().equals(model.getScheduleStatus())) {
            this.changeStatus(model.getId());
        }
        scheduleRepository.setStatus(model.getId(), model.getScheduleStatus());
    }

    private SchedulePageRequest getAndSetIdWithModelAndPo(SchedulePageRequest model, Schedule schedule) {
        if (model.getIsEdit() != null && model.getIsEdit()) {
            scheduleRepository.setId(schedule.getId(), model.getId());
        } else {
            model.setId(schedule.getId());
        }
        scheduleRepository.setGroup(model.getId(), ScheduleEnum.GROUP.getCode() + model.getId());
        return model;
    }

    private Schedule saveSchedule(SchedulePageRequest model) {
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

    private Schedule modelToPo(SchedulePageRequest model) {
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

    private SchedulePageRequest poToModel(Schedule schedule) {
        SchedulePageRequest model = new SchedulePageRequest();
        model.setName(schedule.getName());
        model.setType(schedule.getType());
        model.setNoticeTime(schedule.getNoticeTime());
        model.setStartTime(schedule.getStartTime());
        model.setEndTime(schedule.getEndTime());
        model.setId(schedule.getId());
        model.setScheduleStatus(schedule.getStatus());
        return model;
    }

    private JobDetail getJob(SchedulePageRequest model) {
        JobKey jobKey = new JobKey(ScheduleEnum.JOB.getCode() + model.getId(), ScheduleEnum.GROUP.getCode() + model.getId());
        JobDetail job = null;
        String fullName = "com.tgfc.library.schedule.job." + JobTypeEnum.codeToName(model.getType());
        try {
            job = JobBuilder.newJob(Class.forName(fullName).asSubclass(AbstractJob.class))
                    .withIdentity(jobKey)
                    .usingJobData("id", model.getId())
                    .usingJobData("jobType", model.getType())
                    .build();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return job;
    }

    @Override
    public BaseResponse changeStatus(int id) {
        builder = new BaseResponse.Builder();
        Schedule schedule = scheduleRepository.getById(id);
        if (schedule == null) {
            return builder.message(MessageUtil.getMessage("schedule.findNoData")).status(false).build();
        }
        JobKey jobKey = new JobKey(ScheduleEnum.JOB.getCode() + schedule.getId(), ScheduleEnum.GROUP.getCode() + schedule.getId());
        try {
            if (ScheduleStatusEnum.ENABLE.getCode().equals(schedule.getStatus())) {
                myScheduler.pauseJob(jobKey);
                scheduleRepository.pauseJob(id);
                builder.message(MessageUtil.getMessage("schedule.changeStatusToDisable"));
            } else if (ScheduleStatusEnum.DISABLE.getCode().equals(schedule.getStatus())) {
                myScheduler.resumeJob(jobKey);
                scheduleRepository.resumeJob(id);
                builder.message(MessageUtil.getMessage("schedule.changeStatusToEnable"));
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
        return builder.message(MessageUtil.getMessage("schedule.deleteAll")).status(true).build();
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
        return builder.content(true).status(true).message(MessageUtil.getMessage("schedule.pauseAll")).build();
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
        return builder.content(true).status(true).message(MessageUtil.getMessage("schedule.resumeAll")).build();
    }

    @Override
    public BaseResponse delete(int id) {
        builder = new BaseResponse.Builder();
        Schedule schedule = scheduleRepository.getById(id);
        if (schedule == null) {
            return builder.message(MessageUtil.getMessage("schedule.findNoData")).status(false).build();
        }
        JobKey jobKey = new JobKey(ScheduleEnum.JOB.getCode() + schedule.getId(), ScheduleEnum.GROUP.getCode() + schedule.getId());
        try {
            builder.content(myScheduler.deleteJob(jobKey));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        scheduleRepository.deleteById(id);
        return builder.message(MessageUtil.getMessage("schedule.deleteSuccess")).status(true).build();
    }

    @Override
    public BaseResponse edit(SchedulePageRequest model) {
        builder = new BaseResponse.Builder();
        if (scheduleRepository.getById(model.getId()) == null) {
            return builder.message(MessageUtil.getMessage("schedule.findNoData")).status(false).build();
        }
        model.setIsEdit(true);
        model.setLastExecute(scheduleRepository.getLastExecute(model.getId()));
        this.delete(model.getId());
        return builder.content(this.create(model).getStatus()).message(MessageUtil.getMessage("schedule.editSuccess")).status(true).build();
    }

}
