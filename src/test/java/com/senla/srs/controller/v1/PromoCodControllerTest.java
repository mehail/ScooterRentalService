package com.senla.srs.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.srs.controller.v1.util.AuthProvider;
import com.senla.srs.entity.PromoCod;
import com.senla.srs.mapper.PromoCodMapper;
import com.senla.srs.repository.PromoCodRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    private PromoCodController promoCodController;
    @Autowired
    private PromoCodRepository promoCodRepository;
    @Autowired
    private PromoCodMapper promoCodMapper;
    @Autowired
    private AuthProvider authProvider;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        PromoCod getPromoCod = getPromoCodWitName(TEST_GET_NAME);
        PromoCod deletePromoCod = getPromoCodWitName(TEST_DELETE_NAME);

        promoCodRepository.findByName(getPromoCod.getName())
                .ifPresent(promoCod -> promoCodRepository.save(promoCod));
        promoCodRepository.findByName(deletePromoCod.getName())
                .ifPresent(promoCod -> promoCodRepository.save(promoCod));
    }

    private PromoCod getPromoCodWitName(String name) {
        return new PromoCod(name, LocalDate.of(2021, 1, 1), LocalDate.of(2021, 1, 2), 1, 0, true);
    }


    @Test
    void contextLoads() {
        assertThat(promoCodController).isNotNull();
    }

    @Test
    void getAll200() throws Exception {
        mockMvc.perform(
                get(String.format("%s?page=0&size=%d", URI, pageSize))
                        .headers(authProvider.getResponseHeader(authProvider.getAdminToken())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getAll403() throws Exception {
        mockMvc.perform(get(URI))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void createOrUpdate200() throws Exception {
        PromoCod promoCod = getPromoCodWitName(TEST_POST_NAME);

        mockMvc.perform(
                post(URI)
                        .headers(authProvider.getResponseHeader(authProvider.getAdminToken()))
                        .content(objectMapper.writeValueAsString(promoCodMapper.toDto(promoCod)))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void createOrUpdate403() throws Exception {
        PromoCod promoCod = getPromoCodWitName(TEST_POST_NAME);

        mockMvc.perform(
                post(URI)
                        .content(objectMapper.writeValueAsString(promoCodMapper.toDto(promoCod)))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isForbidden());
    }


    @AfterEach
    public void tearDown() {
        promoCodRepository.findByName(TEST_GET_NAME)
                .ifPresent(promoCod -> promoCodRepository.delete(promoCod));
        promoCodRepository.findByName(TEST_POST_NAME)
                .ifPresent(promoCod -> promoCodRepository.delete(promoCod));
        promoCodRepository.findByName(TEST_DELETE_NAME)
                .ifPresent(promoCod -> promoCodRepository.delete(promoCod));
    }
}