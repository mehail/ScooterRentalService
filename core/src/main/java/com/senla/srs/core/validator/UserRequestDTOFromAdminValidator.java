package com.senla.srs.core.validator;

import com.senla.srs.core.dto.user.UserRequestDTO;
import com.senla.srs.core.entity.UserStatus;
import com.senla.srs.core.entity.security.Role;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service("userRequestDTOFromAdminValidator")
public class UserRequestDTOFromAdminValidator implements Validator {

    private static final String REG_EX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"" +
            "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\" +
            "x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5" +
            "]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?" +
            ":[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    @Override
    public boolean supports(Class<?> aClass) {
        return UserRequestDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        var userRequestDTO = (UserRequestDTO) o;

        if (!userRequestDTO.getEmail().matches(REG_EX)) {
            errors.rejectValue("email", "Email must comply with RFC822");
        }

        if (userRequestDTO.getRole() == Role.ADMIN && userRequestDTO.getStatus() == UserStatus.BANNED) {
            errors.rejectValue("status", "Admin cannot ban other admins");
        }

    }

}
