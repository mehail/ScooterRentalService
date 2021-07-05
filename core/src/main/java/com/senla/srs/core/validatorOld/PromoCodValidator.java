package com.senla.srs.core.validatorOld;

import com.senla.srs.core.dto.promocod.PromoCodDTO;
import com.senla.srs.core.entity.PromoCod;
import org.springframework.validation.Errors;

import java.util.Optional;

public interface PromoCodValidator {

    PromoCodDTO validate(PromoCodDTO promoCodDTO, Optional<PromoCod> optionalPromoCod, Errors errors);

}
