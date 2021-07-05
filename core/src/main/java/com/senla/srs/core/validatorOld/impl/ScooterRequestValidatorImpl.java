package com.senla.srs.core.validatorOld.impl;

import com.senla.srs.core.dto.scooter.ScooterRequestDTO;
import com.senla.srs.core.entity.PointOfRental;
import com.senla.srs.core.entity.ScooterType;
import com.senla.srs.core.validatorOld.ScooterRequestValidator;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.Optional;

@Service
public class ScooterRequestValidatorImpl implements ScooterRequestValidator {

    @Override
    public ScooterRequestDTO validate(ScooterRequestDTO scooterRequestDTO,
                                      Optional<PointOfRental> optionalPointOfRentalDTO,
                                      Optional<ScooterType> optionalScooterTypeDTO,
                                      Errors errors) {
        if (optionalPointOfRentalDTO.isEmpty()) {
            errors.reject("pointOfRentalId", "Point of rentalId with this ID does not exist");
        }

        if (optionalScooterTypeDTO.isEmpty()) {
            errors.reject("typeId", "Scooter type with this ID does not exist");
        }

        return scooterRequestDTO;
    }

}
