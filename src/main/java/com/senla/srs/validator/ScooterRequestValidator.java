package com.senla.srs.validator;

import com.senla.srs.dto.scooter.ScooterRequestDTO;
import com.senla.srs.entity.PointOfRental;
import com.senla.srs.entity.ScooterType;
import org.springframework.validation.Errors;

import java.util.Optional;

public interface ScooterRequestValidator {

    ScooterRequestDTO validate(ScooterRequestDTO scooterRequestDTO,
                               Optional<PointOfRental> optionalPointOfRentalDTO,
                               Optional<ScooterType> optionalScooterTypeDTO,
                               Errors errors);

}
