package com.tgfc.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tgfc.library.LibraryApplication;
import com.tgfc.library.entity.Book;
import com.tgfc.library.entity.Reservation;
import com.tgfc.library.request.PageableRequest;
import com.tgfc.library.request.ReservationPageRequest;
import com.tgfc.library.util.MessageUtil;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = LibraryApplication.class)
class ReservationControllerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    MockHttpSession session;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    MessageUtil messageUtil;

    @BeforeEach
    void init() throws Exception {
        Map<String, String> param = new HashMap<>();
        param.put("account", "ROOT");
        param.put("password", "12345678");

        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(param));
        session = (MockHttpSession) mockMvc.perform(requestBuilder).andReturn().getRequest().getSession();
    }

    @Test
    void testSelectByKeyword() throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("keyword", "法師");
        param.put("pageNumber", 0);
        param.put("pageSize", 10);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/reservation/select").contentType(MediaType.APPLICATION_JSON).session(session).content(objectMapper.writeValueAsString(param));
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertEquals(MessageUtil.getMessage("reservation.searchReservationSuccess"), new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    void testGetBook() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/reservation/getBook").contentType(MediaType.APPLICATION_JSON).session(session).param("id", "1");
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertEquals(MessageUtil.getMessage("reservation.receiveBookSuccess"), new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    @Order(3)
    void testGetBookFail() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/reservation/getBook").contentType(MediaType.APPLICATION_JSON).session(session).param("id", "3");
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertEquals(MessageUtil.getMessage("reservation.checkReservationStatus"), new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    public void testFindAll() throws Exception {
        PageableRequest request = new PageableRequest();

        Gson gson = new GsonBuilder().create();
        String jsonData = gson.toJson(request);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/reservation/findAll").contentType(MediaType.APPLICATION_JSON).session(session).content(jsonData);
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertEquals(MessageUtil.getMessage("reservation.searchSuccess"), new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    public void testFindByDate() throws Exception {
        ReservationPageRequest reservation = new ReservationPageRequest();
        reservation.setStartDate(new Date(2019, 9, 4));
        reservation.setEndDate(new Date(2019, 9, 10));

        Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();
        String jsonData = gson.toJson(reservation);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/reservation/findByDate").contentType(MediaType.APPLICATION_JSON).session(session).content(jsonData);
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertEquals(MessageUtil.getMessage("reservation.searchSuccess"), new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    public void testFindByEmpName() throws Exception {
        PageableRequest request = new PageableRequest();

        Gson gson = new GsonBuilder().create();
        String jsonData = gson.toJson(request);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/reservation/findByEmp").contentType(MediaType.APPLICATION_JSON).session(session).content(jsonData);
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertEquals(MessageUtil.getMessage("reservation.searchSuccess"), new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    public void testInsert() throws Exception {
        Reservation reservation = new Reservation();
        Book book = new Book();
        book.setId(3);
        reservation.setBook(book);

        Gson gson = new GsonBuilder().create();
        String jsonData = gson.toJson(reservation);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/reservation/insert").contentType(MediaType.APPLICATION_JSON).session(session).content(jsonData);
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertEquals(MessageUtil.getMessage("reservation.addWaitSuccess"), new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    @Order(1)
    public void testInsertExsitedReservation() throws Exception {
        Reservation reservation = new Reservation();
        Book book = new Book();
        book.setId(1);
        reservation.setBook(book);

        Gson gson = new GsonBuilder().create();
        String jsonData = gson.toJson(reservation);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/reservation/insert").contentType(MediaType.APPLICATION_JSON).session(session).content(jsonData);
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertEquals(MessageUtil.getMessage("reservation.reservationExisted"), new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    @Order(2)
    public void testInsertExistedReservationByOthers() throws Exception {
        Reservation reservation = new Reservation();
        Book book = new Book();
        book.setId(2);
        reservation.setBook(book);

        Gson gson = new GsonBuilder().create();
        String jsonData = gson.toJson(reservation);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/reservation/insert").contentType(MediaType.APPLICATION_JSON).session(session).content(jsonData);
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertEquals(MessageUtil.getMessage("reservation.addWaitSuccess"), new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    public void testFindByDateWithEmpId() throws Exception {
        ReservationPageRequest reservation = new ReservationPageRequest();
        reservation.setStartDate(new Date(2019, 9, 4));
        reservation.setEndDate(new Date(2019, 9, 10));

        Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();
        String jsonData = gson.toJson(reservation);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/reservation/findByDateWithEmpId").contentType(MediaType.APPLICATION_JSON).session(session).content(jsonData);
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertEquals(MessageUtil.getMessage("reservation.searchSuccess"), new JSONObject(response.getContentAsString()).get("message"));
    }
}