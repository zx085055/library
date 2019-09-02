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
public class ScheduleService implements IScheduleService, Serializable {

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
        response.setData(schedule2Response(list));
        response.setMessage("查詢成功");
        response.setStatus(true);
        return response;
    }

    /**
     * schedule轉為SchedulePageResponse
     */
    private List<SchedulePageResponse> schedule2Response(List<Schedule> list) {
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
    public Boolean create(SchedulePageRequset model) {
        try {
            Schedule schedule = model2Po(model);
//            schedule.setEmployee(employeeRepository.findById(ContextUtil.getPrincipal().toString()).get());
            schedule.setStatus(ScheduleStatus.DISABLE.getCode());
            Schedule scheduleWithId = scheduleRepository.save(schedule);
            model.setId(scheduleWithId.getId());

            JobDetail job = getJob(model);
            CronTrigger trigger = oneDayOneTimeTrigger.getTrigger(model);
            myScheduler.addJob(job, trigger);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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


    private JobDetail getJob(SchedulePageRequset model) {
        JobKey jobKey = new JobKey(model.getName(), model.getName() + model.getId());
        JobDetail job = JobBuilder.newJob(JobTypeEnum.RESERVATION_EXPIRED.getCode().equals(model.getType()) ? ReservationExpiredJob.class :
                JobTypeEnum.LENDING_NEARLY_EXPIRED.getCode().equals(model.getType()) ? LendingNearlyExpiredJob.class :
                        LendingExpiredJob.class)
                .withIdentity(jobKey)
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
        TriggerKey triggerKey = new TriggerKey("OneDayOneTime", schedule.getName() + schedule.getId());
        schedule.setStatus(ScheduleStatus.ENABLE.getCode().equals(schedule.getStatus())
                ? ScheduleStatus.DISABLE.getCode() : ScheduleStatus.ENABLE.getCode());
        scheduleRepository.save(schedule);
        if (ScheduleStatus.ENABLE.getCode().equals(schedule.getStatus())) {
            try {
                myScheduler.unscheduleJob(triggerKey);
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        } else if (ScheduleStatus.DISABLE.getCode().equals(schedule.getStatus())) {
            try {
                myScheduler.rescheduleJob(triggerKey, oneDayOneTimeTrigger.getTrigger(schedule));
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
        response.setData(true);
        response.setMessage("刪除成功");
        response.setStatus(true);
        return response;
    }

    @Override
    public BaseResponse deleteAllJobs() {
        BaseResponse response = new BaseResponse();
        response.setData(myScheduler.deleteAllJobs());
        response.setMessage("全部刪除完成");
        response.setStatus(true);
        return response;
    }


    /**
     * 刪除排程
     */
    @Override
    public Boolean delete(Integer id) {
        return null;
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
