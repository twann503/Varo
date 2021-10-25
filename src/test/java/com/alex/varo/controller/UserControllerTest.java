package com.alex.varo.controller;

import com.alex.varo.model.User;
import com.alex.varo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    private final String BASE_URL = "/v1/users/";
    private final ObjectMapper objectMapper = new ObjectMapper();
    @MockBean
    UserService userService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void listAllUsersWhenTableNotEmptyTest() throws Exception {
        // given
        List<User> mockUsersList = new ArrayList<>();
        mockUsersList.add(new User(1L, "alex", "pham", "alexpham@varo.com"));
        mockUsersList.add(new User(2L, "alex", "pham", "alexpham@varo.com"));
        mockUsersList.add(new User(3L, "alex", "pham", "alexpham@varo.com"));
        when(userService.getAllUsers()).thenReturn(mockUsersList);

        // expect
        MockHttpServletRequestBuilder request = get(BASE_URL).contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andDo(print());
    }

    @Test
    public void listAllUsersWhenTableEmptyTest() throws Exception {
        // given
        List<User> mockUserList = new ArrayList<>();
        when(userService.getAllUsers()).thenReturn(mockUserList);

        // expect
        MockHttpServletRequestBuilder request = get(BASE_URL).contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)))
                .andDo(print());
    }

    @Test
    public void createUserTest() throws Exception {
        // given
        User testUser = new User(1L, "alex", "pham", "alexpham@varo.com");
        when(userService.createUser(testUser)).thenReturn(testUser);

        // expect
        MockHttpServletRequestBuilder request = post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(testUser));
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(testUser)))
                .andDo(print());
    }

    @Test
    public void getUserInfoTest() throws Exception {
        // given
        User testUser = new User(5L, "billy", "pham", "billy@varo.com");
        when(userService.getUserInfo(testUser.getId())).thenReturn(Optional.of(testUser));

        // expect
        MockHttpServletRequestBuilder request = get(BASE_URL + testUser.getId());
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(testUser)))
                .andDo(print());
    }

    @Test
    public void getUserInfoNotExistTest() throws Exception {
        // given
        Long testUserId = 18L;
        when(userService.getUserInfo(testUserId)).thenReturn(Optional.empty());

        // expect
        MockHttpServletRequestBuilder request = get(BASE_URL + testUserId);
        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void userExistTest() throws Exception {
        // given
        String testUserEmail = "alex@varo.com";
        when(userService.isUserExist(testUserEmail)).thenReturn(true);

        // expect
        String queryString = "?email=" + testUserEmail;
        MockHttpServletRequestBuilder request = get(BASE_URL + queryString);
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print());
    }

    @Test
    public void userNotExistTest() throws Exception {
        // given
        String testUserEmail = "alex@varo.com";
        when(userService.isUserExist(testUserEmail)).thenReturn(false);

        // expect
        String queryString = "?email=" + testUserEmail;
        MockHttpServletRequestBuilder request = get(BASE_URL + queryString);
        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(content().string(""))
                .andDo(print());
    }

    @Test
    public void deleteExistingUserTest() throws Exception {
        // given
        Long userId = 123L;
        doNothing().when(userService).deleteUser(userId);

        // expect
        MockHttpServletRequestBuilder request = delete(BASE_URL + userId);
        mockMvc.perform(request)
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    public void updateUserEmailAndArchiveTest() throws Exception {
        // given
        Long userId = 1L;
        String userEmail = "alex@varo.com";

        List<String> pastEmail = new ArrayList<>();
        pastEmail.add("billy@varo.com");

        User testUser = new User(1L, "billy", "pham", "billy@varo.com");

        when(userService.getUserInfo(userId)).thenReturn(Optional.of(testUser));

        testUser.setPastEmails(pastEmail);
        when(userService.updateUserEmail(userId, userEmail)).thenReturn(testUser);

        // expect
        MockHttpServletRequestBuilder request = put(BASE_URL + userId)
                .contentType(MediaType.APPLICATION_JSON).content("{\"email\": \"alex@varo.com\"}");
        mockMvc.perform(request)
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
