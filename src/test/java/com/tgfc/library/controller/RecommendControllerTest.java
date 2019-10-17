package com.tgfc.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tgfc.library.LibraryApplication;
import com.tgfc.library.request.RecommendPageRequest;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = LibraryApplication.class)
public class RecommendControllerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private  MockMvc mockMvc;

    MockHttpSession session;

    @BeforeEach
    public void init() throws Exception{
        Map<String, String> param = new HashMap<>();
        param.put("account", "ROOT");
        param.put("password", "12345678");
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(param));
        session = (MockHttpSession) mockMvc.perform(requestBuilder).andReturn().getRequest().getSession();
    }

    @Test
    public void testSelect() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/recommend/select").contentType(MediaType.APPLICATION_JSON).session(session).content("{}");
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
    }

    @Test
    public void testInsert() throws Exception{
        RecommendPageRequest recommendModel = new RecommendPageRequest();
        recommendModel.setName("NightMare");
        recommendModel.setAuther("Steven King");
        recommendModel.setPubHouse("KingStore");
        recommendModel.setIsbn("12345678911");
        recommendModel.setPublishDate(new Date());

        Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();
        String jsonData = gson.toJson(recommendModel);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/recommend/insert1").contentType(MediaType.APPLICATION_JSON).session(session).content(jsonData);
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals("成功新增一筆",new JSONObject(response.getContentAsString()).get("message"));

    }

    @Test
    public void testInsertExistedRecommend() throws Exception{
        RecommendPageRequest recommendModel = new RecommendPageRequest();
        recommendModel.setName("鮑伯森的奇幻漂流");
        recommendModel.setAuther("鮑伯森");
        recommendModel.setPubHouse("中時旺旺");
        recommendModel.setIsbn("1238787878888");
        recommendModel.setPublishDate(new Date());

        Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();
        String jsonData = gson.toJson(recommendModel);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/recommend/insert").contentType(MediaType.APPLICATION_JSON).session(session).content(jsonData);
        mockMvc.perform(requestBuilder);
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();

        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals("已存在此推薦",new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    public void testInsertExsitedBook() throws Exception{
        RecommendPageRequest recommendModel = new RecommendPageRequest();
        recommendModel.setName("行雲流水");
        recommendModel.setAuther("聖嚴法師");
        recommendModel.setPubHouse("法鼓");
        recommendModel.setIsbn("9576330998");
        recommendModel.setPublishDate(new Date());

        Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();
        String jsonData = gson.toJson(recommendModel);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/recommend/insert").contentType(MediaType.APPLICATION_JSON).session(session).content(jsonData);
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals("已存在此本書籍",new JSONObject(response.getContentAsString()).get("message"));

    }



    @Test
    public void testDelete() throws Exception{

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/recommend/delete").contentType(MediaType.APPLICATION_JSON).session(session).param("id","1");
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals("成功刪除一筆",new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    public void testDeleteNonexistent() throws Exception{

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/recommend/delete").contentType(MediaType.APPLICATION_JSON).session(session).param("id","2");
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
        Assertions.assertEquals("無此推薦",new JSONObject(response.getContentAsString()).get("message"));
    }

}
