package com.tgfc.library.repository;

import com.tgfc.library.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface IReservationRepository extends JpaRepository<Reservation,Integer> {

    @Override
    Reservation getOne(Integer integer);

    @Override
    Page<Reservation> findAll(Pageable pageable);

    @Query("SELECT r from Reservation r where r.startDate<=?1 AND r.endDate>=?2")
    Page<Reservation> findByTimeInterval(Date startDate,Date endDate, Pageable pageable);
}
