package com.tgfc.library.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgfc.library.LibraryApplication;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
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
public class SecurityConfigTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;

    @Mock
    SecurityConfig.ApiConfig config = Mockito.mock(SecurityConfig.ApiConfig.class);

    @Autowired
    WebApplicationContext webApplicationContext;

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
    public void testLoginFilter() throws Exception {
        Map<String, String> param = new HashMap<>();
        param.put("account", "ROOT");
        param.put("password", "12345678");

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(param));


        MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertNotNull(response.getWriter());
    }

    @Test
    public void testForbiddenEntryPointHandler() throws Exception {
        Map<String, String> param = new HashMap<>();
        param.put("account", "");
        param.put("password", "");

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.post("/announcement/select").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(param));


        MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatus());
        Assertions.assertEquals("請登入後再使用", new JSONObject(response.getContentAsString()).get("message"));
        Assertions.assertNotNull(response.getWriter());
    }

    @Test
    public void testLogoutFilter() throws Exception {
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.post("/logout").contentType(MediaType.APPLICATION_JSON);

        MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertEquals("登出成功", new JSONObject(response.getContentAsString()).get("message"));
    }



    @Test
    public void testGetCurrentAuditor()throws Exception{

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
        Assertions.assertNotNull(config.getCurrentAuditor());

    }


}
