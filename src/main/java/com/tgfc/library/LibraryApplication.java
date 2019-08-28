package com.tgfc.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages ={"com.tgfc.library.*","tw.tgfc.common.spring.ldap.conf"})
@EnableJpaRepositories(basePackages = "com.tgfc.library.repository" )
@EntityScan(basePackages = {"com.tgfc.library.entity"})
@EnableJpaAuditing
public class LibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }

}
