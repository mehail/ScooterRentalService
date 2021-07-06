package com.senla.srs.core.validator;

import com.senla.srs.core.dto.promocod.PromoCodDTO;
import com.senla.srs.core.service.PromoCodService;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service("promoCodValidator")
public class PromoCodValidator implements Validator {

    private final PromoCodService promoCodService;

    public PromoCodValidator(PromoCodService promoCodService) {
        this.promoCodService = promoCodService;
    }

    @Override
    public boolean supports(@NonNull Class<?> aClass) {
        return PromoCodDTO.class.equals(aClass);
    }

    @Override
    public void validate(@NonNull Object o, @NonNull Errors errors) {

        var promoCodDTO = (PromoCodDTO) o;

        if (promoCodService.retrievePromoCodByName(promoCodDTO.getName()).isPresent()) {
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

    }

}
