package com.senla.srs.mapper;

import com.senla.srs.dto.user.UserRequestDTO;
import com.senla.srs.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserRequestMapper extends AbstractMapper<User, UserRequestDTO> {

    UserRequestMapper() {
        super(User.class, UserRequestDTO.class);
    }

    public User toEntity(UserRequestDTO dto, Long id) {
        User entity = toEntity(dto);
        entity.setId(id);

        return entity;
    }

}
