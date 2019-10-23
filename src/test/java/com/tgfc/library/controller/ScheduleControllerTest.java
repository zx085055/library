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
public class ScheduleControllerTest {
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
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/schedule/list").contentType(MediaType.APPLICATION_JSON).session(session).content("{}");
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals("查詢成功",new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    void testSelectByTimeInterval() throws Exception{
        Map<String, String> param = new HashMap<>();
        param.put("name","name");
        param.put("startTime", "2019-09-04");
        param.put("endTime", "2019-09-06");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/schedule/list").contentType(MediaType.APPLICATION_JSON).session(session).content(objectMapper.writeValueAsString(param));
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals("查詢成功",new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    void testInsert() throws Exception{
        Map<String, Object> param = new HashMap<>();
        param.put("type", "2");
        param.put("name", "分頁測試用");
        param.put("startTime", "2019-09-05");
        param.put("endTime", "2019-09-06");
        param.put("noticeTime", "14:57:00");
        param.put("scheduleStatus", "2");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/schedule/create").contentType(MediaType.APPLICATION_JSON).session(session).content(objectMapper.writeValueAsString(param));
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals("新增排程no.2成功",new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    void testInsertError() throws Exception{
        Map<String, Object> param = new HashMap<>();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/schedule/create").contentType(MediaType.APPLICATION_JSON).session(session).content(objectMapper.writeValueAsString(param));
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals("排程參數異常",new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    void testChangeStatus() throws Exception{
        Map<String, Object> param = new HashMap<>();
        param.put("id", "1");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/schedule/changeStatus").contentType(MediaType.APPLICATION_JSON).session(session).content(objectMapper.writeValueAsString(param));
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals("狀態改變成功，排程由啟用變為停用",new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    void testChangeStatusWithNoSchedule() throws Exception{
        Map<String, Object> param = new HashMap<>();
        param.put("id", "2");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/schedule/changeStatus").contentType(MediaType.APPLICATION_JSON).session(session).content(objectMapper.writeValueAsString(param));
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals("查無此排程",new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    void testDelete() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/schedule/delete").contentType(MediaType.APPLICATION_JSON).session(session).param("id","1");
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals("刪除指定排程成功",new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    void testDeleteWithNoSchedule() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/schedule/delete").contentType(MediaType.APPLICATION_JSON).session(session).param("id","2");
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals("查無此排程",new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    void testEditStatus() throws Exception{
        Map<String, Object> param = new HashMap<>();
        param.put("id", "1");
        param.put("type", "2");
        param.put("name", "分頁測試用");
        param.put("startTime", "2019-09-05");
        param.put("endTime", "2019-09-06");
        param.put("noticeTime", "14:57:00");
        param.put("scheduleStatus", "2");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/schedule/edit").contentType(MediaType.APPLICATION_JSON).session(session).content(objectMapper.writeValueAsString(param));
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals("修改成功",new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    void testEditStatusWithNoSchedule() throws Exception{
        Map<String, Object> param = new HashMap<>();
        param.put("id", "2");
        param.put("type", "2");
        param.put("name", "分頁測試用");
        param.put("startTime", "2019-09-05");
        param.put("endTime", "2019-09-06");
        param.put("noticeTime", "14:57:00");
        param.put("scheduleStatus", "2");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/schedule/edit").contentType(MediaType.APPLICATION_JSON).session(session).content(objectMapper.writeValueAsString(param));
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals("查無此排程",new JSONObject(response.getContentAsString()).get("message"));
    }








}
