package com.senla.srs.validator.user;

import com.senla.srs.dto.user.UserRequestDTO;
import com.senla.srs.model.UserStatus;
import com.senla.srs.model.security.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
@Component
public class UserRequestCreateValidator extends UserRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return UserRequestDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserRequestDTO userRequestDTO = (UserRequestDTO) o;

        if (userRequestDTO.getBalance() != 0) {
            errors.reject("balance", "The balance must be 0");
        }

        if (userRequestDTO.getRole() != Role.USER) {
            errors.reject("role", "Role must be USER");
        }

        if (userRequestDTO.getStatus() != UserStatus.ACTIVE) {
            errors.reject("status", "Status must be ACTIVE");
        }

    }

}
