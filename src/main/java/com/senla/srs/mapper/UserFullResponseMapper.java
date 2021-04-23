package com.senla.srs.mapper;

import com.senla.srs.dto.user.UserFullResponseDTO;
import com.senla.srs.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserFullResponseMapper extends AbstractMapper<User, UserFullResponseDTO> {
    public UserFullResponseMapper() {
        super(User.class, UserFullResponseDTO.class);
    }

    public Page<UserFullResponseDTO> mapPageToDtoPage(Page<User> users) {
        List<UserFullResponseDTO> userFullResponseDTOS = users.getContent().stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(userFullResponseDTOS);
    }
}
