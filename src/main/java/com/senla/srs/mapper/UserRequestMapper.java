package com.senla.srs.mapper;

import com.senla.srs.dto.user.UserRequestDTO;
import com.senla.srs.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserRequestMapper extends AbstractMapper<User, UserRequestDTO> {
    private final int encryptionStrength;

    UserRequestMapper(@Value("${jwt.encryption.strength}") int encryptionStrength) {
        super(User.class, UserRequestDTO.class);
        this.encryptionStrength = encryptionStrength;
    }

    @Override
    public User toEntity(UserRequestDTO dto) {
        dto.setPassword(cryptPassword(dto.getPassword()));
        return super.toEntity(dto);
    }

    public String cryptPassword(String rowPassword) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(encryptionStrength);
        return bCryptPasswordEncoder.encode(rowPassword);
    }
}
