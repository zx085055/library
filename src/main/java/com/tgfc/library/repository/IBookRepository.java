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

    @Query(value = "SELECT u FROM Book u WHERE u.name LIKE :keyword or u.author LIKE :keyword or u.pubHouse LIKE :keyword ")
    Page<Book> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "SELECT u FROM Book u WHERE u.id = :id")
    Book getById( Integer id);

    @Query(value = "SELECT u FROM Book u WHERE u.isbn LIKE :Isbn")
    Book findByIsbn(@Param("Isbn") String Isbn);

    @Query(value = "SELECT b From Book b where b.name LIKE CONCAT('%',?1,'%') OR b.author LIKE CONCAT('%',?1,'%') OR b.pubHouse LIKE CONCAT('%',?1,'%')")
    Page<Book> findBookByKeyWord (String keyWord,Pageable pageable);


}
