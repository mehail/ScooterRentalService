package com.senla.srs.core.validator;

import com.senla.srs.core.dto.scooter.type.MakerDTO;
import com.senla.srs.core.dto.scooter.type.ScooterTypeRequestDTO;
import org.springframework.validation.Errors;

import java.util.Optional;

public interface ScooterTypeRequestValidator {

    ScooterTypeRequestDTO validate(ScooterTypeRequestDTO scooterTypeRequestDTO,
                                   Optional<MakerDTO> optionalMakerDTO,
                                   Errors errors);

}
