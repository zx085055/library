package com.tgfc.library.repository;

import com.tgfc.library.entity.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IAnnouncementRepository extends JpaRepository<Announcement,Integer> {
    @Query("SELECT r from Announcement r where r.title like CONCAT('%',?1,'%')")
    Page<Announcement> getAnnouncementsByNameLike(String name, Pageable pageable);

}
