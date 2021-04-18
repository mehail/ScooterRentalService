package com.senla.srs.mapper;

import com.senla.srs.dto.user.UserRequestDTO;
import com.senla.srs.model.User;
import com.senla.srs.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRequestMapper extends AbstractMapper<User, UserRequestDTO> {
    private final UserService userService;
    private final int encryptionStrength;

    UserRequestMapper(UserService userService, @Value("${jwt.encryption.strength}") int encryptionStrength) {
        super(User.class, UserRequestDTO.class);
        this.userService = userService;
        this.encryptionStrength = encryptionStrength;
    }

    @Override
    public User toEntity(UserRequestDTO dto) {
        User user = super.toEntity(dto);

        Optional<User> optionalUser = userService.retrieveUserByEmail(dto.getEmail());

        optionalUser.ifPresent(existUser -> user.setId(existUser.getId()));

        user.setPassword(cryptPassword(dto.getPassword()));

        return user;
    }

    public String cryptPassword(String rowPassword) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(encryptionStrength);
        return bCryptPasswordEncoder.encode(rowPassword);
    }
}
