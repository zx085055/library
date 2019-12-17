package com.tgfc.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tgfc.library.LibraryApplication;
import com.tgfc.library.request.PageableRequest;
import com.tgfc.library.request.RecordsSearchPageRequest;
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
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
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
        Assertions.assertEquals(MessageUtil.getMessage("records.findAllSuccess"),new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    void testSelect() throws Exception{
        Map<String, Object> param = new HashMap<>();
        param.put("keyword", "");
        param.put("status", 1);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/records/select").contentType(MediaType.APPLICATION_JSON).session(session).content(objectMapper.writeValueAsString(param));
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals(MessageUtil.getMessage("records.selectSuccess"),new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    void testDeleteNotFound() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/records/delete").contentType(MediaType.APPLICATION_JSON).session(session).param("id","999");
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals(MessageUtil.getMessage("records.findNoData"),new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    void testDelete() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/records/delete").contentType(MediaType.APPLICATION_JSON).session(session).param("id","1");
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals(MessageUtil.getMessage("records.deleteSuccess"),new JSONObject(response.getContentAsString()).get("message"));
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
        Assertions.assertEquals(MessageUtil.getMessage("records.returnNoticeSuccess"),new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    void testReturnBook() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/records/returnBook").contentType(MediaType.APPLICATION_JSON).session(session).param("id","1");
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals(MessageUtil.getMessage("records.returnSuccess"),new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    void testFindAll() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/records/findAll").contentType(MediaType.APPLICATION_JSON).session(session).content("{}");
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals(MessageUtil.getMessage("records.searchSuccess"),new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    public void testFindByDate() throws Exception{
        RecordsSearchPageRequest records = new RecordsSearchPageRequest();
        records.setStartDate(new Date(2019,9,3));
        records.setEndDate(new Date(2019,9,10));

        Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();
        String jsonData = gson.toJson(records);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/records/findByDate").contentType(MediaType.APPLICATION_JSON).session(session).content(jsonData);
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals(MessageUtil.getMessage("records.searchSuccess"),new JSONObject(response.getContentAsString()).get("message"));

    }

    @Test
    public void testFindByEmpId() throws Exception{
        PageableRequest request = new PageableRequest();

        Gson gson = new GsonBuilder().create();
        String jsonData = gson.toJson(request);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/records/findByEmpId").contentType(MediaType.APPLICATION_JSON).session(session).content(jsonData);
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals(MessageUtil.getMessage("records.searchSuccess"),new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    public void testFindByDateWithEmpId() throws Exception{
        RecordsSearchPageRequest request = new RecordsSearchPageRequest();
        request.setStartDate(new Date(2019,9,3));
        request.setEndDate(new Date(2019,9,10));

        Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();
        String jsonData = gson.toJson(request);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/records/findByDateWithEmpId").contentType(MediaType.APPLICATION_JSON).session(session).content(jsonData);
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals(MessageUtil.getMessage("records.searchSuccess"),new JSONObject(response.getContentAsString()).get("message"));

    }

}