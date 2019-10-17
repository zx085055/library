package com.tgfc.library.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tgfc.library.LibraryApplication;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;

@AutoConfigureMockMvc

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = LibraryApplication.class)
public class LoginFilterTest {

    @Autowired
    MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testAttemptAuthentication()throws Exception{

        Map<String, String> param = new HashMap<>();
        param.put("account", "ROOT");
        param.put("password", "12345678");
        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_FORM_URLENCODED).content(objectMapper.writeValueAsString(param));
        MockHttpServletResponse response = mockMvc.perform(request).andReturn().getResponse();
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
        Assertions.assertEquals("方法不支援", new JSONObject(response.getContentAsString()).get("message"));
        Assertions.assertNotNull(response.getWriter());

    }

}
