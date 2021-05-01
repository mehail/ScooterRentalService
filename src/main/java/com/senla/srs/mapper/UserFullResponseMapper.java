package com.senla.srs.mapper;

import com.senla.srs.dto.user.UserFullResponseDTO;
import com.senla.srs.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserFullResponseMapper extends AbstractMapperWithPagination<User, UserFullResponseDTO> {
    public UserFullResponseMapper() {
        super(User.class, UserFullResponseDTO.class);
    }
}
