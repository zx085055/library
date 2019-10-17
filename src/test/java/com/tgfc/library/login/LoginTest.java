package com.tgfc.library.login;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgfc.library.LibraryApplication;
import com.tgfc.library.entity.Employee;
import com.tgfc.library.repository.IEmployeeRepository;
import com.tgfc.library.security.LdapAuthProvider;
import com.tgfc.library.security.LoginFilter;
import com.tgfc.library.security.SecurityConfig;
import org.aspectj.lang.annotation.Before;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@AutoConfigureMockMvc

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = LibraryApplication.class)
public class LoginTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SecurityConfig.ApiConfig config;

//    @MockBean
//    HttpSecurity httpSecurity;

    @Autowired
    WebApplicationContext webApplicationContext;


    @Test//帳號密碼皆對，且存在資料表中，測試假資料
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
    public void testConfigure() throws Exception {
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
    public void testMock() throws Exception {
//        LoginFilter loginFilter = config.loginFilter();
//        Assertions.assertNotNull(loginFilter);
    }
}
