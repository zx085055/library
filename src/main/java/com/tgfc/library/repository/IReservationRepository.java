package com.tgfc.library.repository;

import com.tgfc.library.entity.Reservation;
import com.tgfc.library.request.ReservationPageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.List;

@Repository
public interface IReservationRepository extends JpaRepository<Reservation,Integer> {

   @Query("SELECT r from Reservation r where r.startDate>=?1 AND r.endDate<=?2")
    Page<Reservation> findByTimeInterval(Date startDate,Date endDate,Pageable pageable);

    @Query(value = "select r from Reservation r inner join r.book b where b.id = ?1 and r.employee.id = ?2")
    Reservation findByBookId(Integer bookId,String empId);

    @Modifying
    @Query(value = "update Reservation r set r.status=2 where r.id=?1")
    int cancleReservation(Integer reservationId);
}
