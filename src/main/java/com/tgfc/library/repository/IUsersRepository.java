package com.tgfc.library.repository;

import com.tgfc.library.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUsersRepository extends JpaRepository<Users,String> {





    Users findByAccount(String name);

}

