package com.senla.srs.mapper;

import com.senla.srs.dto.UserRequestDTO;
import com.senla.srs.model.User;
import com.senla.srs.model.UserStatus;
import com.senla.srs.model.security.Role;
import com.senla.srs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserRequestMapper extends AbstractMapper<User, UserRequestDTO> {
    @Value("${jwt.encryption.strength}")
    private int encryptionStrength;
    private UserService userService;

    @Autowired
    UserRequestMapper(UserService userService) {
        super(User.class, UserRequestDTO.class);
        this.userService = userService;
    }

    @Override
    public User toEntity(UserRequestDTO dto) {
        User user = super.toEntity(dto);

        Optional<User> optionalUser = userService.retrieveUserByEmail(dto.getEmail());
        String cryptPassword = crypt(dto.getPassword());

        if (optionalUser.isPresent()) {
            user.setId(optionalUser.get().getId());
        } else {
            user.setId(null);
        }

        if (optionalUser.isEmpty()) {

            user.setPassword(cryptPassword);
            user.setBalance(0);
            user.setRole(Role.USER);
            user.setStatus(UserStatus.ACTIVE);

            //ToDo Если пользователь есть, по email
        }

        return user;
    }

    private boolean isMatchPassword(Optional<User> optionalUser, String cryptPassword) {
        return optionalUser.get().getPassword().equals(cryptPassword);
    }

    private String crypt(String rowPassword) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(encryptionStrength);
        return bCryptPasswordEncoder.encode(rowPassword);
    }
}
