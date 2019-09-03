package com.tgfc.library.repository;

import com.tgfc.library.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface IReservationRepository extends JpaRepository<Reservation, Integer> {

    @Query("SELECT r FROM Reservation r LEFT JOIN r.book b LEFT JOIN r.employee e WHERE r.employee.name LIKE CONCAT('%',?1,'%') OR b.author LIKE CONCAT('%',?1,'%') OR b.name LIKE CONCAT('%',?1,'%')")
    Page<Reservation> getRecordsByNameLikeAndStatus(String keyword, Pageable pageable);

    @Query("SELECT r from Reservation r where r.startDate>=?1 AND r.endDate<=?2")
    Page<Reservation> findByTimeInterval(Date startDate, Date endDate, Pageable pageable);

    @Query(value = "select r from Reservation r inner join r.book b where b.id = ?1 and r.employee.id = ?2")
    Reservation findByBookId(Integer bookId, String empId);

    @Modifying
    @Query(value = "update Reservation r set r.status=?1 where r.id=?2")
    int changeReservationStatus(Integer status,Integer reservationId);

    @Query(value = "select r from Reservation r where r.endDate>=?1")
    List<Reservation> reservationExpiredList(Date endDate);

    @Query(value = "select count(r) from Reservation r inner join  r.book b where b.id = ?1 and r.status = ?2")
    Integer reservationStatusCount(Integer bookId,Integer status);

}
