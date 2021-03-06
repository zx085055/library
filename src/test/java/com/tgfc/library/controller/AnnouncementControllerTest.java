package com.tgfc.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgfc.library.LibraryApplication;
import com.tgfc.library.util.MessageUtil;
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

import java.util.HashMap;
import java.util.Map;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = LibraryApplication.class)
class AnnouncementControllerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    MockHttpSession session;



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
        Assertions.assertEquals(MessageUtil.getMessage("announcement.selectSuccess"),new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    void testSelectByTimeInterval() throws Exception{
        Map<String, String> param = new HashMap<>();
        param.put("startTime", "2019-09-04");
        param.put("endTime", "2019-09-06");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/announcement/select").contentType(MediaType.APPLICATION_JSON).session(session).content(objectMapper.writeValueAsString(param));
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals(MessageUtil.getMessage("announcement.selectSuccess"),new JSONObject(response.getContentAsString()).get("message"));
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
        Assertions.assertEquals(MessageUtil.getMessage("announcement.insertSuccess"),new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    void testInsertError() throws Exception{
        Map<String, Object> param = new HashMap<>();
        param.put("title", "我是公告");
        param.put("context", "我是內容");
        param.put("startTime", "2019-09-05");
        param.put("endTime", "2019-08-06");
        param.put("status", true);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/announcement/insert").contentType(MediaType.APPLICATION_JSON).session(session).content(objectMapper.writeValueAsString(param));
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals(MessageUtil.getMessage("announcement.insertDateError"),new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    void testUpdate() throws Exception{
        Map<String, Object> param = new HashMap<>();
        param.put("id", 2);
        param.put("title", "我是公告");
        param.put("context", "我是內容");
        param.put("startTime", "2019-09-05");
        param.put("endTime", "2019-10-06");
        param.put("status", true);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/announcement/update").contentType(MediaType.APPLICATION_JSON).session(session).content(objectMapper.writeValueAsString(param));
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals(MessageUtil.getMessage("announcement.updateSuccess"),new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    void testDeleteNotFound() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/announcement/delete").contentType(MediaType.APPLICATION_JSON).session(session).param("id","999");
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals(MessageUtil.getMessage("announcement.findNoData"),new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    void testDelete() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/announcement/delete").contentType(MediaType.APPLICATION_JSON).session(session).param("id","1");
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals(MessageUtil.getMessage("announcement.deleteSuccess"),new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    void testChangeStatus() throws Exception{
        Map<String, Object> param = new HashMap<>();
        param.put("id", 2);
        param.put("status", true);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/announcement/changeStatus").contentType(MediaType.APPLICATION_JSON).session(session).content(objectMapper.writeValueAsString(param));
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals(MessageUtil.getMessage("announcement.switchStatusSuccess"),new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    void testSelectNotExpired() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/announcement/findByNotExpired").contentType(MediaType.APPLICATION_JSON).session(session).content("{}");
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals(MessageUtil.getMessage("announcement.selectNotExpiredSuccess"),new JSONObject(response.getContentAsString()).get("message"));
    }

}