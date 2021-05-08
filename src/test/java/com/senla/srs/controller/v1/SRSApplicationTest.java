package com.senla.srs.controller.v1;

import com.senla.srs.SRSApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SRSApplicationTest {
    @Autowired
    private SRSApplication controller;

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }
}
