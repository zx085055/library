package com.tgfc.library.repository;

import com.tgfc.library.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IBookRepository extends JpaRepository<Book,Integer> {

    @Override
    public Book getOne(Integer id);
    @Query(value = "SELECT u FROM Book u WHERE u.name LIKE :keyword")
    Page<Book> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);
    @Query(value = "SELECT u FROM Book u WHERE u.id = :keyword")
    Book getById( Integer keyword);

    @Query(value = "SELECT u FROM Book u WHERE u.isbn LIKE :Isbn")
    Book findByIsbn(@Param("Isbn") String Isbn);


}
