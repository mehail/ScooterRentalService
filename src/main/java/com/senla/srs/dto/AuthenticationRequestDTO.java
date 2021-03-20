package com.senla.srs.dto;

import lombok.Data;

//ToDo пока не используется
@Data
public class AuthenticationRequestDTO {
    private String email;
    private String password;
}
