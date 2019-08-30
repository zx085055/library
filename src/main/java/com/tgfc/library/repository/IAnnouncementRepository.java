package com.tgfc.library.repository;

import com.tgfc.library.entity.Announcement;
import com.tgfc.library.response.BaseResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IAnnouncementRepository extends JpaRepository<Announcement,Integer> {
    @Query("SELECT r from Recommend r where r.name like CONCAT('%',?1,'%')")
    BaseResponse getAnnouncementsByNameLike(String name, Pageable pageable);

}
