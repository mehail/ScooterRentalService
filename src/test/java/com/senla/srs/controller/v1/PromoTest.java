package com.senla.srs.controller.v1;

import com.senla.srs.controller.v1.facade.PromoCodControllerFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PromoTest {
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdHJpbmciLCJyb2xlIjoiQURNSU4iLCJpZCI6NCwiaWF0IjoxNjIwNDYxODI3LCJleHAiOjE2MjEwNjY2Mjd9.YVZiniBQjR35CiA9-RnXRjU2uMqVNn_pPSKYYW7xE_M";

    @Autowired
    private PromoCodControllerFacade controller;

    @Test
    void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    @Test
    void t() throws Exception {
        System.out.println("controller.getAll(0, 5, \"id\") = " + controller.getAll(0, 5, "name", token));
        assertThat(controller).isNotNull();
    }
}
