package com.senla.srs.mapper;

import com.senla.srs.dto.user.UserFullResponseDTO;
import com.senla.srs.model.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserFullResponseMapper extends AbstractMapper<User, UserFullResponseDTO> {
    public UserFullResponseMapper() {
        super(User.class, UserFullResponseDTO.class);
    }

    public List<UserFullResponseDTO> mapListToDtoList(List<User> users) {
        return users.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<UserFullResponseDTO> mapListToDtoList(User user) {
        return mapListToDtoList(Collections.singletonList(user));
    }
}
