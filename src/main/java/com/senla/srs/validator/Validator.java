package com.senla.srs.validator;

import com.senla.srs.dto.AbstractDTO;
import org.springframework.validation.Errors;

import java.util.Optional;

public interface Validator<E, D extends AbstractDTO> {
    D validateDtoFromAdmin(D requestDto, Errors errors);
    D validateNewDto(D requestDto, Errors errors);
    D validateExistDto(D requestDto, Errors errors, Optional<E> optionalDto);
}
