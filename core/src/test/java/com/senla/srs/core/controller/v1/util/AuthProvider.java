package com.senla.srs.core.controller.v1.util;

import org.springframework.http.HttpHeaders;

public interface AuthProvider {

    String getAdminToken();

    String getUserToken();

    HttpHeaders getResponseHeader(String token);

}
