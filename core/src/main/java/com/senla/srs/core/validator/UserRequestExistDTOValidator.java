package com.senla.srs.core.validator;

import com.senla.srs.core.dto.user.UserRequestDTO;
import com.senla.srs.core.entity.User;
import com.senla.srs.core.entity.security.Role;
import com.senla.srs.core.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Service("userRequestExistDTOValidator")
public class UserRequestExistDTOValidator implements Validator {

    private final UserService userService;

    public UserRequestExistDTOValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UserRequestDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        var userRequestDTO = (UserRequestDTO) o;
        Optional<User> optionalUser = userService.retrieveUserByEmail(userRequestDTO.getEmail());

        if (optionalUser.isEmpty()) {
            errors.rejectValue("email", "User with this email was not found");
        } else {
            var existUser = optionalUser.get();

            if (!userRequestDTO.getBalance().equals(existUser.getBalance())) {
                errors.rejectValue("balance", "Balance can only be changed by the Administrator");
            }

        }

        if (userRequestDTO.getRole() != Role.USER) {
            errors.rejectValue("role", "Role can only be changed by the Administrator");
        }

    }
    
}
