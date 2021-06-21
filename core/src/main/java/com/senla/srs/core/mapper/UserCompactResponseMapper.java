package com.senla.srs.core.mapper;

import com.senla.srs.core.dto.user.UserCompactResponseDTO;
import com.senla.srs.core.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserCompactResponseMapper extends AbstractMapper<User, UserCompactResponseDTO> {

    public UserCompactResponseMapper() {
        super(User.class, UserCompactResponseDTO.class);
    }

}