package com.senla.srs.mapper;

import com.senla.srs.dto.user.UserCompactResponseDTO;
import com.senla.srs.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserCompactResponseMapper extends AbstractMapper<User, UserCompactResponseDTO> {

    public UserCompactResponseMapper() {
        super(User.class, UserCompactResponseDTO.class);
    }

}