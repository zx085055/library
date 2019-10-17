package com.tgfc.library.advice;

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
public class ExceptionAdivceTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    MockHttpSession session;

    @BeforeEach
    void init() throws Exception{
        Map<String, String> param = new HashMap<>();
        param.put("account", "TGFC062");
        param.put("password", "Tgfc@3B1234");

        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(param));
        session = (MockHttpSession) mockMvc.perform(requestBuilder).andReturn().getRequest().getSession();
    }

    @Test
    public void accessDeniedTest() throws Exception{

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/recommend/delete").contentType(MediaType.APPLICATION_JSON).session(session).param("id","1");
        mockMvc.perform(requestBuilder);
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.FORBIDDEN.value(),response.getStatus());
        Assertions.assertEquals("權限不足,請檢查授權",new JSONObject(response.getContentAsString()).get("message"));
    }

    @Test
    public void methodArgumentNotValidTest() throws Exception{
        RecommendPageRequest recommendModel = new RecommendPageRequest();
        recommendModel.setName("NightMare");
        recommendModel.setAuther("Steven King");
        recommendModel.setPubHouse("KingStore");
        recommendModel.setIsbn("1");
        recommendModel.setPublishDate(new Date());

        Gson gson = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();
        String jsonData = gson.toJson(recommendModel);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/recommend/insert").contentType(MediaType.APPLICATION_JSON).session(session).content(jsonData);
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatus());
        Assertions.assertEquals("參數錯誤,請檢查參數內容",new JSONObject(response.getContentAsString()).get("message"));
    }
}
