package com.senla.srs.core.validator.impl;

import com.senla.srs.core.dto.geo.CityDTO;
import com.senla.srs.core.dto.pointofrental.PointOfRentalRequestDTO;
import com.senla.srs.core.validator.PointOfRentalRequestValidator;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.Optional;

@Service
public class PointOfRentalRequestValidatorImpl implements PointOfRentalRequestValidator {

    @Override
    public PointOfRentalRequestDTO validate(PointOfRentalRequestDTO pointOfRentalRequestDTO,
                                            Optional<CityDTO> optionalCityDTO,
                                            Errors errors) {

        if (optionalCityDTO.isEmpty()) {
            errors.reject("cityId", "City with this ID does not exist");
        }

        return pointOfRentalRequestDTO;
    }

}
