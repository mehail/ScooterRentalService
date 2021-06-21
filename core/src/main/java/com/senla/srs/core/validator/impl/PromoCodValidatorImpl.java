package com.senla.srs.core.validator.impl;

import com.senla.srs.core.dto.promocod.PromoCodDTO;
import com.senla.srs.core.entity.PromoCod;
import com.senla.srs.core.validator.PromoCodValidator;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.Optional;

@Service
public class PromoCodValidatorImpl implements PromoCodValidator {

    @Override
    public PromoCodDTO validate(PromoCodDTO promoCodDTO, Optional<PromoCod> optionalPromoCod, Errors errors) {

        if (optionalPromoCod.isPresent()) {
            errors.reject("this", "Existing promo code is not available for change");
        } else {

            if (promoCodDTO.getExpiredDate() != null &&
                    !promoCodDTO.getStartDate().isBefore(promoCodDTO.getExpiredDate())) {
                errors.reject("expireDate", "Expire date must be later than start date");
            }

            if (!promoCodDTO.getAvailable()) {
                errors.reject("available", "PromoCod must be available for use when created");
            }

        }

        return promoCodDTO;
    }

}
