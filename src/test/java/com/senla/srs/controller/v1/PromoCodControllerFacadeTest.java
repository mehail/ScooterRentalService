package com.senla.srs.controller.v1;

import com.senla.srs.controller.v1.facade.EntityControllerFacade;
import com.senla.srs.controller.v1.util.AuthProvider;
import com.senla.srs.dto.promocod.PromoCodDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PromoCodControllerFacadeTest {
    @Value("${spring.data.web.pageable.default-page-size}")
    private Integer pageSize;
    @Autowired
    private AuthProvider authProvider;
    @Autowired
    private EntityControllerFacade<PromoCodDTO, PromoCodDTO, PromoCodDTO, String> controller;

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

    @Test
    void getAll() {
        Assertions.assertSame(controller.getAll(0, pageSize, "name", authProvider.getAdminToken()).getClass(), PageImpl.class);
    }
}
