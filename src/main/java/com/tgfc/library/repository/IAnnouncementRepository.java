package com.tgfc.library.repository;

import com.tgfc.library.entity.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface IAnnouncementRepository extends JpaRepository<Announcement, Integer> {
    @Query("SELECT r from Announcement r where r.title like CONCAT('%',?1,'%') AND r.startTime>=?2 AND r.endTime<=?3")
    Page<Announcement> getAnnouncementsByNameLikeAndTimeInterval(String name, Date startTime, Date endTime, Pageable pageable);

    @Query("SELECT r from Announcement r where r.startTime<=?1 AND r.endTime>=?1")
    Page<Announcement> getAnnouncementsByTimeInterval(Date currentTime, Pageable pageable);
}
