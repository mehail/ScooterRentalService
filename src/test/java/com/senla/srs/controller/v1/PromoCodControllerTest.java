package com.senla.srs.controller.v1;

import com.senla.srs.controller.v1.util.AuthProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PromoCodControllerTest {
    private static final String URL = "/api/v1/promo_codes";
    @Autowired
    private PromoCodController promoCodController;
    @Autowired
    private AuthProvider authProvider;
    @Autowired
    private MockMvc mockMvc;
    @Value("${spring.data.web.pageable.default-page-size}")
    private Integer pageSize;

    @Test
    void contextLoads() {
        assertThat(promoCodController).isNotNull();
    }

    @Test
    void getAllAuth() throws Exception {
        mockMvc.perform(
                get(String.format("%s?page=0&size=%d", URL, pageSize))
                        .headers(authProvider.getResponseHeader(authProvider.getAdminToken())))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getAllNoAuth() throws Exception {
        mockMvc.perform(get(URL))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

}