package com.senla.srs.validator;

import com.senla.srs.dto.db.MakerDTO;
import com.senla.srs.dto.scooter.type.ScooterTypeRequestDTO;
import org.springframework.validation.Errors;

import java.util.Optional;

public interface ScooterTypeRequestValidator {

    ScooterTypeRequestDTO validate(ScooterTypeRequestDTO scooterTypeRequestDTO,
                                   Optional<MakerDTO> optionalMakerDTO,
                                   Errors errors);

}
