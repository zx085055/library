package com.tgfc.library.repository;

import com.tgfc.library.entity.Books;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IBookDataRepository extends JpaRepository<Books,Integer> {

    @Override
    public Books getOne(Integer id);
    @Query(value = "SELECT u FROM Books u WHERE u.name LIKE :keyword")
    Page<Books> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
