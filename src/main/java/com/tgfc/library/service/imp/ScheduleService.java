package com.tgfc.library.service.imp;

import com.tgfc.library.entity.Schedule;
import com.tgfc.library.enums.JobTypeEnum;
import com.tgfc.library.repository.IScheduleRepository;
import com.tgfc.library.request.SchedulePageRequset;
import com.tgfc.library.response.SchedulePageResponse;
import com.tgfc.library.schedule.job.LendingNearlyExpiredJob;
import com.tgfc.library.schedule.job.ReservationExpiredJob;
import com.tgfc.library.schedule.scheduler.MyScheduler;
import com.tgfc.library.schedule.trigger.OneDayOneTimeTrigger;
import com.tgfc.library.service.IScheduleService;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Autowired;
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




    /**
     * 動態查詢各參數(排程名稱，起始時間，結束時間)
     */
    @Override
    public List<SchedulePageResponse> list(SchedulePageRequset model) throws ParseException {
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
        return schedule2Response(list);
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
            JobDetail job = JobBuilder.newJob
                    (JobTypeEnum.RESERVATION_EXPIRED.getCode().equals(model.getType())? ReservationExpiredJob.class :
                            JobTypeEnum.LENDING_NEARLY_EXPIRED.getCode().equals(model.getType())? LendingNearlyExpiredJob.class:
                                    ReservationExpiredJob.class)
                    .withIdentity(model.getName(), "group")
                    .build();
            job.getJobDataMap().put("service",this);

            CronTrigger trigger = oneDayOneTimeTrigger.getTrigger(model.getNoticeTime());






        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

    public String print(){
        return "xxx";
    }


}
