package com.senla.srs.controller.v1.util;

import org.springframework.http.HttpHeaders;

public interface TokenProvider {
    String getAdminToken();
    String getUserToken();
    HttpHeaders getResponseHeader(String token);
}
