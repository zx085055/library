package com.tgfc.library.repository;

import com.tgfc.library.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBookRepository extends JpaRepository<Book, Integer> {

    @Override
    Book getOne(Integer id);

    @Query(value = "SELECT b FROM Book b WHERE b.name LIKE concat('%@',:keyword,'%') ESCAPE '@' or b.author LIKE concat('%@',:keyword,'%') ESCAPE '@' or b.pubHouse LIKE concat('%@',:keyword,'%') ESCAPE '@' or b.type LIKE concat('%@',:keyword,'%') ESCAPE '@' or b.isbn = :keyword ")
    Page<Book> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "SELECT b FROM Book b WHERE (b.name LIKE concat('%@',:keyword,'%') ESCAPE '@' or b.author LIKE concat('%@',:keyword,'%') ESCAPE '@' or b.pubHouse LIKE concat('%@',:keyword,'%') ESCAPE '@' or b.type LIKE concat('%@',:keyword,'%') ESCAPE '@' or b.isbn = :keyword) AND b.status<>:checkPermission")
    Page<Book> findNotScrapByKeyword(@Param("keyword") String keyword,  @Param("checkPermission")Integer checkPermission, Pageable pageable);

    @Query(value = "SELECT b FROM Book b WHERE b.status <> 6")
    Page<Book> findAllNotScrapByKeyword(Pageable pageable);

    @Query(value = "SELECT u FROM Book u WHERE u.id = :id")
    Book getById(Integer id);

    @Query(value = "SELECT u FROM Book u WHERE u.isbn = :Isbn")
    List<Book> findByIsbn(@Param("Isbn") String Isbn);

    @Query(value = "SELECT u FROM Book u WHERE u.isbn = :isbn and u.id <> :id")
    List<Book> findByIsbnAndId(@Param("id") Integer id, @Param("isbn") String isbn);

    @Query(value = "SELECT u FROM Book u WHERE u.propertyCode = :propertyCode")
    List<Book> findByPropertyCode(String propertyCode);

    @Query(value = "SELECT u FROM Book u WHERE u.propertyCode = :propertyCode and u.id <> :id")
    List<Book> findByPropertyCodeAndId(Integer id, String propertyCode);

    @Query(value = "SELECT b From Book b where b.name LIKE CONCAT('%@',?1,'%') ESCAPE '@' OR b.author LIKE CONCAT('%',?1,'%') ESCAPE '@' OR b.pubHouse LIKE CONCAT('%',?1,'%') ESCAPE '@' ")
    Page<Book> findBookByKeyWord(String keyWord, Pageable pageable);

    @Query(value = "delete from book where id = :id ", nativeQuery = true)
    void deleteById(@Param("id") Integer id);

    @Query(value = "select b from Book b where b.id=?1 and b.status=?2")
    Book checkBookLended(Integer id, Integer status);

    @Query(value = "select b from Book b where b.id=?1 and b.status in ?2")
    Book checkBookAbnormal(Integer id, List<Integer> status);

}
