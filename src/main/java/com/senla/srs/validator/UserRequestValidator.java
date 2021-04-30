package com.senla.srs.validator;

import com.senla.srs.dto.user.UserRequestDTO;
import com.senla.srs.model.UserStatus;
import com.senla.srs.model.security.Role;
import com.senla.srs.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Data
@Slf4j
@AllArgsConstructor
@Component
public class UserRequestValidator implements Validator {

    private final UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return UserRequestDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserRequestDTO userRequestDTO = (UserRequestDTO) o;

        if (userRequestDTO.getBalance() != 0) {
            errors.reject("balance", "When registering, the balance is 0");
        }

        if (userRequestDTO.getRole() != Role.USER) {
            errors.reject("role", "When registering, the role is USER");
        }

        if (userRequestDTO.getStatus() != UserStatus.ACTIVE) {
            errors.reject("status", "When registering, the status is ACTIVE");
        }

    }

}
