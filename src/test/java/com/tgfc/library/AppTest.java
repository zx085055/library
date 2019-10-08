package com.tgfc.library;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AppTest {

    @BeforeAll
    public static void setUp(){
        System.out.println("setUp");
    }

    @BeforeEach
    public void init(){
        System.out.println("init");
    }


    @Test
    void test(){
        System.out.println("1");
    }

    @Test
    void test2(){
        System.out.println("2");
    }
}
