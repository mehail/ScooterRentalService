package com.senla.srs.validator.impl;

import com.senla.srs.dto.geo.CityDTO;
import com.senla.srs.dto.pointofrental.PointOfRentalRequestDTO;
import com.senla.srs.validator.PointOfRentalRequestValidator;
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
