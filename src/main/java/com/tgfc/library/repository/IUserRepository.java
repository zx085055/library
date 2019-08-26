package com.tgfc.library.repository;

import com.tgfc.library.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User,String> {

    @Override
    public User getOne(String id);

}
