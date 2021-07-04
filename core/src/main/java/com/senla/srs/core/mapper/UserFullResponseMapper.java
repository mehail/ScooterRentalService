package com.senla.srs.core.mapper;

import com.senla.srs.core.dto.user.UserFullResponseDTO;
import com.senla.srs.core.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserFullResponseMapper extends AbstractMapperWithPagination<User, UserFullResponseDTO> {

    public UserFullResponseMapper() {
        super(User.class, UserFullResponseDTO.class);
    }

    @Override
    public UserFullResponseDTO toDto(User entity) {
        var dto = super.toDto(entity);
        dto.setEmail(entity.getAccount().getEmail());
        return dto;
    }
}
