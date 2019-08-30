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
public interface IRecordsRepository extends JpaRepository<Records,Integer> {
    @Override
    Records getOne(Integer integer);

    @Override
    Page<Records> findAll(Pageable pageable);

    @Query(value = "SELECT r.*,book.`name`,book.author,book.publish_date\n" +
            "FROM `records` AS r LEFT JOIN book ON book_id = book.id\n" +
            "WHERE r.borrow_username like CONCAT('%',?1,'%') OR book.author like CONCAT('%',?1,'%') OR book.`name` like CONCAT('%',?1,'%')\n" +
            "AND r.`status`=?2", nativeQuery = true)
    Page<Records> getRecordsByNameLikeAndStatus(String name,Integer status, Pageable pageable);

    @Query("SELECT r from Records r where r.borrowDate<=?1 AND r.returnDate>=?2")
    Page<Reservation> findByTimeInterval(Date borrowDate, Date returnDate, Pageable pageable);
}
