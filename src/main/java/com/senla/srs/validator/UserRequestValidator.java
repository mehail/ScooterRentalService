package com.senla.srs.validator;

import com.senla.srs.dto.user.UserRequestDTO;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UserRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return UserRequestDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserRequestDTO userRequestDTO = (UserRequestDTO) o;
        if (userRequestDTO.getBalance() != 0) {
            errors.reject("balance", "Ты слишком жадный");
        }
    }
}
