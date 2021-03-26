package com.senla.srs.mapper;

import com.senla.srs.dto.UserRequestDTO;
import com.senla.srs.model.User;
import com.senla.srs.model.UserStatus;
import com.senla.srs.model.security.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserRequestMapper extends AbstractMapper<User, UserRequestDTO> {
    @Value("${jwt.encryption.strength}")
    private int encryptionStrength;

    UserRequestMapper() {
        super(User.class, UserRequestDTO.class);
    }

    @Override
    public User toEntity(UserRequestDTO dto) {
        User user = super.toEntity(dto);

        String rowPassword = dto.getPassword();
        String cryptPassword = crypt(rowPassword);

        user.setPassword(cryptPassword);
        user.setBalance(0);
        user.setRole(Role.USER);
        user.setStatus(UserStatus.ACTIVE);

        return user;
    }

    private String crypt(String rowPassword) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(encryptionStrength);
        return bCryptPasswordEncoder.encode(rowPassword);
    }
}
