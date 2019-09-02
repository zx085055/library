package com.tgfc.library.repository;

import com.tgfc.library.entity.Records;
import com.tgfc.library.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface IRecordsRepository extends JpaRepository<Records, Integer> {
    @Override
    Records getOne(Integer integer);

    @Override
    Page<Records> findAll(Pageable pageable);

    @Query("SELECT r FROM Records r LEFT JOIN r.book b WHERE r.borrowUsername LIKE CONCAT('%',?1,'%') OR b.author LIKE CONCAT('%',?1,'%') OR b.name LIKE CONCAT('%',?1,'%') AND r.status=?2")
    Page<Records> getRecordsByNameLikeAndStatus(String name, Integer status, Pageable pageable);

    @Query("SELECT r from Records r where r.borrowDate<=?1 AND r.returnDate>=?2")
    Page<Reservation> findByTimeInterval(Date borrowDate, Date returnDate, Pageable pageable);
}
