package com.tgfc.library.repository;

import com.tgfc.library.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface IScheduleRepository extends JpaRepository<Schedule,Integer> {

    @Query("SELECT r from Schedule r where r.id=?1 ")
    Schedule getById(int id);

    @Modifying
    @Transactional
    @Query(value = "update Schedule r set r.status='1' where r.status='2'")
    int resumeAll();

    @Modifying
    @Transactional
    @Query(value = "update Schedule r set r.status='2' where r.status='1'")
    int pauseAll();

    @Modifying
    @Transactional
    @Query(value = "update Schedule r set r.status='2' where r.status='1' and r.id=?1")
    int pauseJob(int id);

    @Modifying
    @Transactional
    @Query(value = "update Schedule r set r.status='1' where r.status='2' and r.id=?1")
    int resumeJob(int id);

    @Modifying
    @Transactional
    @Query(value = "update Schedule r set r.id=?2 where  r.group=?1")
    int idMinusOne (String group,int id);

    @Modifying
    @Transactional
    @Query(value = "update Schedule r set r.group=?1 where r.id=?2")
    int setGroup (String group,int id);



}
