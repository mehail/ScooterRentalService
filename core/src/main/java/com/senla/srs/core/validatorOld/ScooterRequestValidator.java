package com.senla.srs.core.validatorOld;

import com.senla.srs.core.dto.scooter.ScooterRequestDTO;
import com.senla.srs.core.entity.PointOfRental;
import com.senla.srs.core.entity.ScooterType;
import org.springframework.validation.Errors;

import java.util.Optional;

public interface ScooterRequestValidator {

    ScooterRequestDTO validate(ScooterRequestDTO scooterRequestDTO,
                               Optional<PointOfRental> optionalPointOfRentalDTO,
                               Optional<ScooterType> optionalScooterTypeDTO,
                               Errors errors);

}
