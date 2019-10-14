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
class RecordsControllerTest {

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
    void testAllSelect() throws Exception{
        Map<String, Object> param = new HashMap<>();
        param.put("keyword", "");
        param.put("status", 0);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/records/select").contentType(MediaType.APPLICATION_JSON).session(session).content(objectMapper.writeValueAsString(param));
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals("出借全查成功",new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    void testSelect() throws Exception{
        Map<String, Object> param = new HashMap<>();
        param.put("keyword", "");
        param.put("status", 1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/records/select").contentType(MediaType.APPLICATION_JSON).session(session).content(objectMapper.writeValueAsString(param));
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals("出借查詢成功",new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    void testDeleteNotFound() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/records/delete").contentType(MediaType.APPLICATION_JSON).session(session).param("id","999");
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals("出借無此資料",new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    void testDelete() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/records/delete").contentType(MediaType.APPLICATION_JSON).session(session).param("id","1");
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals("出借刪除成功",new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    void testReturnNotify() throws Exception{
        Map<String, Object> param = new HashMap<>();
        param.put("id", 2);
        param.put("title", "該還書了");
        param.put("context", "快點還書!!!!!");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/records/returnNotify").contentType(MediaType.APPLICATION_JSON).session(session).content(objectMapper.writeValueAsString(param));
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals("還書通知成功",new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    void testReturnBook() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/records/returnBook").contentType(MediaType.APPLICATION_JSON).session(session).param("id","1");
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals("歸還成功",new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    void testFindAll() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/records/findAll").contentType(MediaType.APPLICATION_JSON).session(session).content("{}");
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals("查詢成功",new JSONObject(response.getContentAsString()).get("message"));
    }

}