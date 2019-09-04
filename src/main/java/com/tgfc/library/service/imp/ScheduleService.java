package com.tgfc.library.service.imp;

import com.tgfc.library.entity.Employee;
import com.tgfc.library.entity.Schedule;
import com.tgfc.library.enums.JobTypeEnum;
import com.tgfc.library.enums.ScheduleStatus;
import com.tgfc.library.repository.IEmployeeRepository;
import com.tgfc.library.repository.IScheduleRepository;
import com.tgfc.library.request.SchedulePageRequset;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.response.EmployeeResponse;
import com.tgfc.library.response.SchedulePageResponse;
import com.tgfc.library.schedule.job.LendingExpiredJob;
import com.tgfc.library.schedule.job.LendingNearlyExpiredJob;
import com.tgfc.library.schedule.job.ReservationExpiredJob;
import com.tgfc.library.schedule.scheduler.MyScheduler;
import com.tgfc.library.schedule.trigger.OneDayOneTimeTrigger;
import com.tgfc.library.service.IScheduleService;
import com.tgfc.library.util.ContextUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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

        Path<String> name = rootEntity.get("name");
        Path<Date> startTime = rootEntity.<Date>get("startTime");
        Path<Date> endTime = rootEntity.<Date>get("endTime");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse("1970-01-01");
        Date endDate = sdf.parse("2099-01-01");

        model.setName(model.getName() == null ? "%" : "%" + model.getName() + "%");
        model.setStartTime(model.getStartTime() == null ? startDate : model.getStartTime());
        model.setEndTime(model.getEndTime() == null ? endDate : model.getEndTime());

        Predicate predicate = criteriaBuilder.and(criteriaBuilder.like(name, model.getName()),
                criteriaBuilder.greaterThanOrEqualTo(startTime, model.getStartTime()),
                criteriaBuilder.lessThanOrEqualTo(endTime, model.getEndTime()));

        query = query.select(rootEntity).where(predicate);
        List<Schedule> list = entityManager.createQuery(query).getResultList();
        response.setData(scheduleList2Response(list));
        response.setMessage("查詢成功");
        response.setStatus(true);
        return response;
    }

    /**
     * schedule轉為SchedulePageResponse
     */
    private List<SchedulePageResponse> scheduleList2Response(List<Schedule> list) {
        return list.stream()
                .map(schedule -> {
                    SchedulePageResponse schedulePageResponse = new SchedulePageResponse();
                    schedulePageResponse.setId(schedule.getId());
                    schedulePageResponse.setName(schedule.getName());
                    schedulePageResponse.setStartTime(schedule.getStartTime());
                    schedulePageResponse.setEndTime(schedule.getEndTime());
                    schedulePageResponse.setLastExecute(schedule.getLastExecute());
                    schedulePageResponse.setStatus(schedule.getStatus());
                    return schedulePageResponse;
                }).collect(Collectors.toList());
    }

    /**
     * 新增排程
     */
    @Override
    public BaseResponse create(SchedulePageRequset model) {
        BaseResponse response = new BaseResponse();
        try {
            Schedule schedule = model2Po(model);
            schedule.setEmployee(employeeRepository.findById(ContextUtil.getAccount()).get());
            schedule.setStatus(ScheduleStatus.ENABLE.getCode());
            Schedule scheduleWithId = scheduleRepository.save(schedule);
            model.setId(scheduleWithId.getId());
            JobDetail job = getJob(model);
            CronTrigger trigger = oneDayOneTimeTrigger.getTrigger(model);
            myScheduler.addJob(job, trigger);
            scheduleRepository.setGroup(model.getName() + model.getId(), model.getId());
            response.setData(true);
            response.setMessage("新增排程no."+model.getId()+"成功");
            response.setStatus(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private Schedule model2Po(SchedulePageRequset model) {
        Schedule schedule = new Schedule();
        schedule.setName(model.getName());
        schedule.setType(model.getType());
        schedule.setNoticeTime(model.getNoticeTime());
        schedule.setStartTime(model.getStartTime());
        schedule.setEndTime(model.getEndTime());
        schedule.setStatus(model.getScheduleStatus());
        schedule.setJobName(JobTypeEnum.code2Trans(model.getType()));
        return schedule;
    }

    private SchedulePageRequset po2Model(Schedule schedule) {
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
        JobDetail job = JobBuilder.newJob(JobTypeEnum.RESERVATION_EXPIRED.getCode().equals(model.getType()) ? ReservationExpiredJob.class :
                JobTypeEnum.LENDING_NEARLY_EXPIRED.getCode().equals(model.getType()) ? LendingNearlyExpiredJob.class :
                        LendingExpiredJob.class)
                .withIdentity(jobKey)
                .build();
//        job.getJobDataMap("startDate");
        return job;
    }

    /**
     * 改變排程狀態 ( 啟用 <---> 禁用 )
     */
    @Override
    public BaseResponse changeStatus(int id) {
        BaseResponse response = new BaseResponse();
        Schedule schedule = scheduleRepository.getById(id);
        JobKey jobKey = new JobKey(schedule.getName(), schedule.getName() + schedule.getId());
//        TriggerKey triggerKey = new TriggerKey("OneDayOneTime", schedule.getName() + schedule.getId());
        if (ScheduleStatus.ENABLE.getCode().equals(schedule.getStatus())) {
            myScheduler.pauseJob(jobKey);
            scheduleRepository.pauseJob(id);
            response.setMessage("狀態改變成功，排程"+id+"由"+ScheduleStatus.ENABLE.getTrans()+"變為"+ScheduleStatus.DISABLE.getTrans());
        } else if (ScheduleStatus.DISABLE.getCode().equals(schedule.getStatus())) {
            myScheduler.resumeJob(jobKey);
            scheduleRepository.resumeJob(id);
            response.setMessage("狀態改變成功，排程"+id+"由"+ScheduleStatus.DISABLE.getTrans()+"變為"+ScheduleStatus.ENABLE.getTrans());
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


    /**
     * 刪除排程
     */
    @Override
    public BaseResponse delete(int id) {
        BaseResponse response = new BaseResponse();
        Schedule schedule = scheduleRepository.getById(id);
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
        scheduleRepository.deleteById(model.getId());
        response.setData(this.create(model).getStatus());
        response.setMessage("修改成功");
        response.setStatus(true);
        return response;
    }


    /**********測試用，不會留***********/

    @Override
    public Schedule one() {
        return scheduleRepository.getById(2);
    }

    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    @Override
    public Schedule getone() {
        return scheduleRepository.getOne(1);
    }

    @Override
    public int xxx() {
        return (int) (Math.random() * 42 + 1);
    }

    public void print() {
        System.out.println("hello");
    }


}
