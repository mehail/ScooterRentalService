package com.senla.srs.validator.impl;

import com.senla.srs.dto.db.MakerDTO;
import com.senla.srs.dto.scooter.type.ScooterTypeRequestDTO;
import com.senla.srs.validator.ScooterTypeRequestValidator;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.Optional;

@Service
public class ScooterTypeRequestValidatorImpl implements ScooterTypeRequestValidator {
    @Override
    public ScooterTypeRequestDTO validate(ScooterTypeRequestDTO scooterTypeRequestDTO,
                                          Optional<MakerDTO> optionalMakerDTO,
                                          Errors errors) {

        if (optionalMakerDTO.isEmpty()) {
            errors.reject("makerId", "Maker with this ID does not exist");
        }

        return scooterTypeRequestDTO;
    }
}
