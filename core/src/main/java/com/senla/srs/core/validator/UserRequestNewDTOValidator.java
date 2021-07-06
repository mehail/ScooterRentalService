package com.senla.srs.core.validator;

import com.senla.srs.core.dto.user.UserRequestDTO;
import com.senla.srs.core.entity.UserStatus;
import com.senla.srs.core.entity.security.Role;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Service("userRequestNewDTOValidator")
public class UserRequestNewDTOValidator implements Validator {

    private final UserRequestDTOFromAdminValidator userRequestDTOFromAdminValidator;

    public UserRequestNewDTOValidator(UserRequestDTOFromAdminValidator userRequestDTOFromAdminValidator) {
        this.userRequestDTOFromAdminValidator = userRequestDTOFromAdminValidator;
    }

    @Override
    public boolean supports(@NonNull Class<?> aClass) {
        return UserRequestDTO.class.equals(aClass);
    }

    @Override
    public void validate(@NonNull Object o, @NonNull Errors errors) {

        var userRequestDTO = (UserRequestDTO) o;
        ValidationUtils.invokeValidator(userRequestDTOFromAdminValidator, userRequestDTO, errors);

        if (userRequestDTO.getBalance() != 0) {
            errors.rejectValue("balance", "The balance must be 0");
        }

        if (userRequestDTO.getRole() != Role.USER) {
            errors.rejectValue("role", "Role must be USER");
        }

        if (userRequestDTO.getStatus() != UserStatus.ACTIVE) {
            errors.rejectValue("status", "Status must be ACTIVE");
        }

    }

}
