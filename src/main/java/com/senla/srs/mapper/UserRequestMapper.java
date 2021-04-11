package com.senla.srs.mapper;

import com.senla.srs.dto.user.UserRequestDTO;
import com.senla.srs.model.User;
import com.senla.srs.service.UserService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRequestMapper extends AbstractMapper<User, UserRequestDTO> {
    private final UserService userService;

    UserRequestMapper(UserService userService) {
        super(User.class, UserRequestDTO.class);
        this.userService = userService;
    }

    @Override
    public User toEntity(UserRequestDTO dto) {
        User user = super.toEntity(dto);

        Optional<User> optionalUser = userService.retrieveUserByEmail(dto.getEmail());

        optionalUser.ifPresent(existUser -> user.setId(existUser.getId()));

        user.setPassword(userService.cryptPassword(dto.getPassword()));

        return user;
    }
}
