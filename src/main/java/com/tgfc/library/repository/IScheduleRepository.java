package com.tgfc.library.repository;

import com.tgfc.library.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IScheduleRepository extends JpaRepository<Schedule,Integer> {

    @Query("SELECT r from Schedule r where r.id=?1 ")
    Schedule getById(int id);

//    @Query("SELECT r from Schedule r where r.id=?1 ")
//    Page<Schedule> getList();
}
