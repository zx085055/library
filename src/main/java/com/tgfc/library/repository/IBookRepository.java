package com.tgfc.library.repository;

import com.tgfc.library.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface IBookRepository extends JpaRepository<Book, Integer> {

    @Override
    Book getOne(Integer id);

    @Query(value = "SELECT b FROM Book b WHERE b.name LIKE :keyword or b.author LIKE :keyword or b.pubHouse LIKE :keyword or b.isbn = :isbn ")
    Page<Book> findAllByKeyword(@Param("keyword") String keyword, @Param("isbn") String isbn, Pageable pageable);

    @Query(value = "SELECT u FROM Book u WHERE u.id = :id")
    Book getById(Integer id);

    @Query(value = "SELECT u FROM Book u WHERE u.isbn = :Isbn")
    List<Book> findByIsbn(@Param("Isbn") String Isbn);

    @Query(value = "SELECT b From Book b where b.name LIKE CONCAT('%',?1,'%') OR b.author LIKE CONCAT('%',?1,'%') OR b.pubHouse LIKE CONCAT('%',?1,'%')")
    Page<Book> findBookByKeyWord(String keyWord, Pageable pageable);

    @Query(value = "delete from book  where id LIKE :id ", nativeQuery = true)
    void deleteById(@Param("id") Integer id);

    @Query(value = "select b from Book b where b.id=?1 and b.status=?2")
    Book checkBookLended(Integer id, Integer status);

    @Query(value = "select b from Book b where b.id=?1 and b.status in ?2")
    Book checkBookAbnormal(Integer id, List<Integer> status);

}
