package com.senla.srs.validator;

import com.senla.srs.dto.promocod.PromoCodDTO;
import com.senla.srs.entity.PromoCod;
import org.springframework.validation.Errors;

import java.util.Optional;

public interface PromoCodValidator {

    PromoCodDTO validate(PromoCodDTO promoCodDTO, Optional<PromoCod> optionalPromoCod, Errors errors);

}
