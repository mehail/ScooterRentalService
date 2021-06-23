package com.senla.srs.web.controller.v1.util;

import org.springframework.http.HttpHeaders;

public interface AuthProvider {

    String getAdminToken();

    String getUserToken();

    HttpHeaders getResponseHeader(String token);

}
