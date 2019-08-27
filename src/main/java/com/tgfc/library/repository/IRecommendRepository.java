package com.tgfc.library.repository;

import com.tgfc.library.entity.Recommend;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IRecommendRepository extends JpaRepository<Recommend,Integer> {

    @Query("SELECT r from Recommend r where r.name like CONCAT('%',?1,'%')")
    Page<Recommend> getRecommendsByNameLike(String name, Pageable pageable);

    @Query("SELECT r from Recommend r where r.name=?1")
    Recommend getRecommendByName(String name);

}
