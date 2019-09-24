package com.tgfc.library.repository;

import com.tgfc.library.entity.Records;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface IRecordsRepository extends JpaRepository<Records, Integer> {
    @Override
    Records getOne(Integer integer);

    @Override
    Page<Records> findAll(Pageable pageable);

    @Query("SELECT r FROM Records r LEFT JOIN r.book b WHERE r.borrowUsername LIKE CONCAT('%',?1,'%') OR b.author LIKE CONCAT('%',?1,'%') OR b.name LIKE CONCAT('%',?1,'%') OR r.status=?2")
    Page<Records> getRecordsByNameLikeAndStatus(String name, Integer status, Pageable pageable);

    @Query("SELECT r from Records r where r.borrowDate>=?1 AND r.borrowDate<=?2")
    Page<Records> findByTimeInterval(Date borrowDate, Date returnDate, Pageable pageable);

    @Query(value = "select r from Records r where r.endDate>=?1 and r.status=1")
    List<Records> getLendingExpiredList(Date currentDate);

    @Modifying
    @Transactional
    @Query(value = "update Records r set r.status=3 where r.endDate<=?1 and r.status=1")
    int lendingExpiredStatus(Date currentDate);
}
