package com.senla.srs.core.mapper;

import com.senla.srs.core.dto.user.UserRequestDTO;
import com.senla.srs.core.entity.Account;
import com.senla.srs.core.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserRequestMapper extends AbstractMapper<User, UserRequestDTO> {

    UserRequestMapper() {
        super(User.class, UserRequestDTO.class);
    }

    public User toEntity(UserRequestDTO dto, Long id) {
        User entity = toEntity(dto);

        entity.setId(id);
        entity.setAccount(new Account(null, dto.getEmail(), dto.getPassword()));

        return entity;
    }

}
