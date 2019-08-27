package com.tgfc.library.repository;

import com.tgfc.library.entity.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRecommendRepository extends JpaRepository<Recommend,Integer> {

}
