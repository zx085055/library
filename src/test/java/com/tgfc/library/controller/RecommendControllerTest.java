package com.tgfc.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgfc.library.LibraryApplication;
import com.tgfc.library.request.RecommendPageRequest;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = LibraryApplication.class)
public class RecommendControllerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    MockHttpSession session;

    @Autowired
    WebApplicationContext webApplicationContext;

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
    public void testInsert() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/recommend/select").contentType(MediaType.APPLICATION_JSON).session(session).content("{}");
        MockHttpServletResponse response = mockMvc.perform(requestBuilder).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(),response.getStatus());
    }
}
