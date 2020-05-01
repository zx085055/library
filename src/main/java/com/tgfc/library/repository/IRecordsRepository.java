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

    @Query(value = "SELECT * FROM `records` WHERE records.id = (SELECT MAX(id) FROM `records` where records.book_id = ?1)",nativeQuery = true)
    Records checkOwnBorrowed(Integer id);

    @Query("SELECT r FROM Records r LEFT JOIN r.book b WHERE (r.borrowUsername LIKE CONCAT('%',?1,'%') ESCAPE '/' OR b.name LIKE CONCAT('%',?1,'%') ESCAPE '/') AND r.status=?2")
    Page<Records> getRecordsByNameLikeAndStatus(String name, Integer status, Pageable pageable);

    @Query("SELECT r FROM Records r LEFT JOIN r.book b WHERE r.borrowUsername LIKE CONCAT('%',?1,'%') ESCAPE '/' OR b.name LIKE CONCAT('%',?1,'%') ESCAPE '/'")
    Page<Records> getRecordsByNameLike(String name, Pageable pageable);

    @Query("SELECT r from Records r where r.borrowDate>=?1 AND r.borrowDate<=?2")
    Page<Records> findByTimeInterval(Date borrowDate, Date returnDate, Pageable pageable);

    @Query(value = "SELECT * FROM `records` WHERE Date(records.end_date) BETWEEN  CURRENT_DATE and  DATE_ADD(CURRENT_DATE,INTERVAL 3 DAY) and records.`status` = 1;", nativeQuery = true)
    List<Records> getLendingNearlyExpiredList();

    @Query(value = "SELECT * FROM `records` WHERE Date(records.end_date) <  CURRENT_DATE and records.`status` in (1,3)", nativeQuery = true)
    List<Records> getLendingExpiredList();

    @Query(value = "select r from Records r where r.borrowId = ?1")
    Page<Records> getRecordsByEmpId(String EmpId, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "update `records` r set r.status=3 WHERE Date(r.end_date) <  CURRENT_DATE  and r.status=1", nativeQuery = true)
    int lendingExpiredStatus();

    @Query("SELECT r from Records r where r.borrowId=?1 And r.borrowDate>=?2 AND r.borrowDate<=?3")
    Page<Records> findByTimeIntervalWithEmpId(String empId, Date startDate, Date endDate, Pageable pageable);

}
