package com.senla.srs.core.validator;

import com.senla.srs.core.dto.user.UserRequestDTO;
import com.senla.srs.core.entity.User;
import org.springframework.validation.Errors;

import java.util.Optional;

public interface UserRequestValidator {

    UserRequestDTO validateDtoFromAdmin(UserRequestDTO requestDto, Errors errors);

    UserRequestDTO validateNewDto(UserRequestDTO requestDto, Errors errors);

    UserRequestDTO validateExistDto(UserRequestDTO requestDto, Errors errors, Optional<User> optionalDto);

}
