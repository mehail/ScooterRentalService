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

        if (!isExistUser(optionalUser)) {
            user.setPassword(cryptPassword);
            user.setBalance(0);
            user.setRole(Role.USER);
            user.setStatus(UserStatus.ACTIVE);
        } else if (isMatchPassword(optionalUser, cryptPassword)){

            User existUser = optionalUser.get();

            user.setId(existUser.getId());

            if (dto.getNewEmail() != null) {
                user.setEmail(dto.getNewEmail());
            }

            if (dto.getNewPassword() != null) {
                user.setEmail(dto.getNewPassword());
            }

            user.setFirstName(dto.getFirstName());
            user.setLastName(dto.getLastName());
            user.setRole(existUser.getRole());
            user.setStatus(existUser.getStatus());
            user.setBalance(existUser.getBalance());
        }

        System.out.println(user);

        return user;
    }

    private boolean isExistUser(Optional<User> optionalUser) {
        return optionalUser.isPresent();
    }

    private boolean isMatchPassword(Optional<User> optionalUser, String cryptPassword) {
        return optionalUser.get().getPassword().equals(cryptPassword);
    }

    private String crypt(String rowPassword) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(encryptionStrength);
        return bCryptPasswordEncoder.encode(rowPassword);
    }
}
