package com.tgfc.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgfc.library.LibraryApplication;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import java.util.HashMap;
import java.util.Map;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = LibraryApplication.class)
class AnnouncementControllerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    MockHttpSession session;

    @Autowired
    WebApplicationContext webApplicationContext;

    @BeforeEach
    void init() throws Exception{
        Map<String, String> param = new HashMap<>();
        param.put("account", "ROOT");
        param.put("password", "12345678");

        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(param));
        session = (MockHttpSession) mockMvc.perform(requestBuilder).andReturn().getRequest().getSession();
    }

    @Test
    void testSelect() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/announcement/select").contentType(MediaType.APPLICATION_JSON).session(session).content("{}");
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals("公告查詢成功",new JSONObject(response.getContentAsString()).get("message"));

        Map<String, String> param = new HashMap<>();
        param.put("startTime", "2019-09-04");
        param.put("endTime", "2019-09-06");

        requestBuilder = MockMvcRequestBuilders.post("/announcement/select").contentType(MediaType.APPLICATION_JSON).session(session).content(objectMapper.writeValueAsString(param));
        response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals("公告查詢成功",new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    void testInsert() throws Exception{
        Map<String, Object> param = new HashMap<>();
        param.put("title", "我是公告");
        param.put("context", "我是內容");
        param.put("startTime", "2019-09-05");
        param.put("endTime", "2019-09-06");
        param.put("status", true);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/announcement/insert").contentType(MediaType.APPLICATION_JSON).session(session).content(objectMapper.writeValueAsString(param));
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals("新增公告成功",new JSONObject(response.getContentAsString()).get("message"));

        param.put("endTime", "2019-08-06");

        requestBuilder = MockMvcRequestBuilders.post("/announcement/insert").contentType(MediaType.APPLICATION_JSON).session(session).content(objectMapper.writeValueAsString(param));
        response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals("新增公告日期有誤",new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    void testUpdate() throws Exception{
        Map<String, Object> param = new HashMap<>();
        param.put("id", 91);
        param.put("title", "我是公告");
        param.put("context", "我是內容");
        param.put("startTime", "2019-09-05");
        param.put("endTime", "2019-10-06");
        param.put("status", true);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/announcement/update").contentType(MediaType.APPLICATION_JSON).session(session).content(objectMapper.writeValueAsString(param));
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals("公告編輯成功",new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    void testDelete() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/announcement/delete").contentType(MediaType.APPLICATION_JSON).session(session).param("id","98");
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals("公告刪除成功",new JSONObject(response.getContentAsString()).get("message"));

        requestBuilder = MockMvcRequestBuilders.delete("/announcement/delete").contentType(MediaType.APPLICATION_JSON).session(session).param("id","999");
        response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals("公告刪除失敗",new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    void testChangeStatus() throws Exception{
        Map<String, Object> param = new HashMap<>();
        param.put("id", 91);
        param.put("status", false);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/announcement/changeStatus").contentType(MediaType.APPLICATION_JSON).session(session).content(objectMapper.writeValueAsString(param));
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals("公告狀態切換成功",new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    void testSelectNotExpired() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/announcement/findByNotExpired").contentType(MediaType.APPLICATION_JSON).session(session).content("{}");
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals("公告未過期查詢成功",new JSONObject(response.getContentAsString()).get("message"));
    }

}