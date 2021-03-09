package com.senla.srs.controllers.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/registration")
    public String createUser() {
        return "Hello";
    }
}
