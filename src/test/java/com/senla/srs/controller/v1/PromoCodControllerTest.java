package com.senla.srs.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.srs.controller.v1.util.AuthProvider;
import com.senla.srs.entity.PromoCod;
import com.senla.srs.mapper.PromoCodMapper;
import com.senla.srs.service.PromoCodService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PromoCodControllerTest {

    private static final String URI = "/api/v1/promo_codes";
    private static final String TEST_GET_NAME = "testPromoCod1";
    private static final String TEST_POST_NAME = "testPromoCod2";
    private static final String TEST_DELETE_NAME = "testPromoCod3";
    @Value("${spring.data.web.pageable.default-page-size}")
    private Integer pageSize;
    @Autowired
    private PromoCodService promoCodService;
    @Autowired
    private PromoCodMapper promoCodMapper;
    @Autowired
    private AuthProvider authProvider;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    private PromoCod getPromoCodWitName(String name) {
        return new PromoCod(name, LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 2), 1, 0, true);
    }


    @Nested
    class GetAll {

        @Test
        void getAllAdminAuth200() throws Exception {
            mockMvc.perform(
                    get(String.format("%s?page=0&size=%d", URI, pageSize))
                            .headers(authProvider.getResponseHeader(authProvider.getAdminToken())))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        void getAllNoAuth403() throws Exception {
            mockMvc.perform(
                    get(String.format("%s?page=0&size=%d", URI, pageSize)))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }

        @Test
        void getAllUserAuth403() throws Exception {
            mockMvc.perform(
                    get(String.format("%s?page=0&size=%d", URI, pageSize))
                            .headers(authProvider.getResponseHeader(authProvider.getUserToken())))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }

    }

    @Nested
    class Get {

        @BeforeEach
        public void setUp() {
            PromoCod getPromoCod = getPromoCodWitName(TEST_GET_NAME);

            if (promoCodService.retrievePromoCodByName(TEST_GET_NAME).isEmpty()) {
                promoCodService.save(getPromoCod);
            }

        }

        @Test
        void getByNameAuth200() throws Exception {
            mockMvc.perform(
                    get(String.format("%s/%s", URI, TEST_GET_NAME))
                            .headers(authProvider.getResponseHeader(authProvider.getAdminToken())))
                    .andDo(print())
                    .andExpect(jsonPath("$.name").value(TEST_GET_NAME))
                    .andExpect(status().isOk());
        }

        @Test
        void getByNameNonAuth403() throws Exception {
            mockMvc.perform(
                    get(String.format("%s/%s", URI, TEST_GET_NAME)))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }

        @Test
        void getByNameNonExistName404() throws Exception {
            mockMvc.perform(
                    get(String.format("%s/%s", URI, "nonExistName"))
                            .headers(authProvider.getResponseHeader(authProvider.getAdminToken())))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @AfterEach
        public void tearDown() {
            promoCodService.retrievePromoCodByName(TEST_GET_NAME)
                    .ifPresent(promoCod -> promoCodService.deleteByName(promoCod.getName()));
        }

    }

    @Nested
    class Post {

        @BeforeEach
        public void setUp() {
            promoCodService.retrievePromoCodByName(TEST_POST_NAME)
                    .ifPresent(promoCod -> promoCodService.deleteByName(promoCod.getName()));
        }

        @Test
        void createOrUpdateAdminAuth200() throws Exception {
            PromoCod promoCod = getPromoCodWitName(TEST_POST_NAME);

            mockMvc.perform(
                    post(URI)
                            .headers(authProvider.getResponseHeader(authProvider.getAdminToken()))
                            .content(objectMapper.writeValueAsString(promoCodMapper.toDto(promoCod)))
                            .contentType(MediaType.APPLICATION_JSON)
            )
                    .andDo(print())
                    .andExpect(status().isCreated());
        }

        @Test
        void createOrUpdateNoValid400() throws Exception {
            PromoCod promoCod = getPromoCodWitName(TEST_POST_NAME);
            promoCod.setAvailable(false);

            mockMvc.perform(
                    post(URI)
                            .headers(authProvider.getResponseHeader(authProvider.getAdminToken()))
                            .content(objectMapper.writeValueAsString(promoCodMapper.toDto(promoCod)))
                            .contentType(MediaType.APPLICATION_JSON)
            )
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void createOrUpdateNonAuth403() throws Exception {
            PromoCod promoCod = getPromoCodWitName(TEST_POST_NAME);

            mockMvc.perform(
                    post(URI)
                            .content(objectMapper.writeValueAsString(promoCodMapper.toDto(promoCod)))
                            .contentType(MediaType.APPLICATION_JSON)
            )
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }

        @Test
        void createOrUpdateUserAuth403() throws Exception {
            PromoCod promoCod = getPromoCodWitName(TEST_POST_NAME);

            mockMvc.perform(
                    post(URI)
                            .headers(authProvider.getResponseHeader(authProvider.getUserToken()))
                            .content(objectMapper.writeValueAsString(promoCodMapper.toDto(promoCod)))
                            .contentType(MediaType.APPLICATION_JSON)
            )
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }

        @AfterEach
        public void tearDown() {
            promoCodService.retrievePromoCodByName(TEST_POST_NAME)
                    .ifPresent(promoCod -> promoCodService.deleteByName(promoCod.getName()));
        }
    }

    @Nested
    class Delete {

        @BeforeEach
        public void setUp() {
            PromoCod getPromoCod = getPromoCodWitName(TEST_DELETE_NAME);

            if (promoCodService.retrievePromoCodByName(TEST_DELETE_NAME).isEmpty()) {
                promoCodService.save(getPromoCod);
            }
        }

        @Test
        void deleteAdminAuth202() throws Exception {
            mockMvc.perform(
                    delete(String.format("%s/%s", URI, TEST_DELETE_NAME))
                            .headers(authProvider.getResponseHeader(authProvider.getAdminToken())))
                    .andDo(print())
                    .andExpect(status().isAccepted());
        }

        @Test
        void deleteNonExistName400() throws Exception {
            mockMvc.perform(
                    delete(String.format("%s/%s", URI, "nonExistName"))
                            .headers(authProvider.getResponseHeader(authProvider.getAdminToken())))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void deleteNoAuth403() throws Exception {
            mockMvc.perform(
                    delete(String.format("%s/%s", URI, TEST_DELETE_NAME)))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }

        @Test
        void deleteUserAuth403() throws Exception {
            mockMvc.perform(
                    delete(String.format("%s/%s", URI, TEST_DELETE_NAME))
                            .headers(authProvider.getResponseHeader(authProvider.getUserToken())))
                    .andDo(print())
                    .andExpect(status().isForbidden());
        }

        @AfterEach
        public void tearDown() {
            promoCodService.retrievePromoCodByName(TEST_DELETE_NAME)
                    .ifPresent(promoCod -> promoCodService.deleteByName(promoCod.getName()));
        }

    }

}