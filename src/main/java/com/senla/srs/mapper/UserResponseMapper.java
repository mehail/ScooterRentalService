package com.senla.srs.mapper;

import com.senla.srs.dto.user.UserResponseDTO;
import com.senla.srs.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserResponseMapper extends AbstractMapper<User, UserResponseDTO> {
    public UserResponseMapper() {
        super(User.class, UserResponseDTO.class);
    }
}
