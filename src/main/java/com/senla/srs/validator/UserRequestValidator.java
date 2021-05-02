package com.senla.srs.validator;

import com.senla.srs.dto.user.UserRequestDTO;
import com.senla.srs.entity.User;
import com.senla.srs.entity.UserStatus;
import com.senla.srs.entity.security.Role;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.Optional;

@Service
public class UserRequestValidator implements Validator<User, UserRequestDTO> {

    @Override
    public UserRequestDTO validateDtoFromAdmin(UserRequestDTO requestDto, Errors errors) {

        String regEx = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\" +
                "x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-" +
                "9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-" +
                "9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0" +
                "c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

        if (!requestDto.getEmail().matches(regEx)) {
            errors.reject("email", "Email must comply with RFC822");
        }

        return requestDto;
    }

    @Override
    public UserRequestDTO validateNewDto(UserRequestDTO requestDto, Errors errors) {
        validateDtoFromAdmin(requestDto, errors);

        if (requestDto.getBalance() != 0) {
            errors.reject("balance", "The balance must be 0");
        }

        if (requestDto.getRole() != Role.USER) {
            errors.reject("role", "Role must be USER");
        }

        if (requestDto.getStatus() != UserStatus.ACTIVE) {
            errors.reject("status", "Status must be ACTIVE");
        }

        return requestDto;
    }

    @Override
    public UserRequestDTO validateExistDto(UserRequestDTO requestDto, Errors errors, Optional<User> optionalDto) {
        if (optionalDto.isEmpty()) {
            errors.reject("email", "User with this email was not found");
        } else {
            User existUser = optionalDto.get();

            if (!requestDto.getBalance().equals(existUser.getBalance())) {
                errors.reject("balance", "Balance can only be changed by the Administrator");
            }
        }

        if (requestDto.getRole() != Role.USER) {
            errors.reject("role", "Role can only be changed by the Administrator");
        }

        return requestDto;
    }
}
