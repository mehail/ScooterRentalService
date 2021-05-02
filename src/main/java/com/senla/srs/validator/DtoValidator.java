package com.senla.srs.validator;

import com.senla.srs.dto.AbstractDTO;
import org.springframework.validation.Errors;

import java.util.Optional;

public interface DtoValidator<E, D extends AbstractDTO> {
    void validateDtoFromAdmin(D requestDto, Errors errors);
    void validateNewDto(D requestDto, Errors errors);
    void validateExistDto(D requestDto, Errors errors, Optional<E> optionalDto);
}
