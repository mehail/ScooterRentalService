package com.senla.srs.validator.impl;

import com.senla.srs.dto.promocod.PromoCodDTO;
import com.senla.srs.validator.PromoCodDtoValidator;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

@Service
public class PromoCodDtoValidatorImpl implements PromoCodDtoValidator {
    @Override
    public PromoCodDTO validate(PromoCodDTO promoCodDTO, Errors errors) {
        if (promoCodDTO.getExpiredDate() != null &&
                !promoCodDTO.getStartDate().isBefore(promoCodDTO.getExpiredDate())) {
            errors.reject("expireDate", "Expire date must be later than start date");
        }

        if (!promoCodDTO.getAvailable()) {
            errors.reject("available", "PromoCod must be available for use when created");
        }

        return promoCodDTO;
    }
}
