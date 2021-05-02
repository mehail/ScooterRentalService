package com.senla.srs.validator;

import com.senla.srs.dto.promocod.PromoCodDTO;
import org.springframework.validation.Errors;

public interface PromoCodDtoValidator {
    PromoCodDTO validate(PromoCodDTO promoCodDTO, Errors errors);
}
