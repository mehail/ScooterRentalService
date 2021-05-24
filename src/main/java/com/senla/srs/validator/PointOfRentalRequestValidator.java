package com.senla.srs.validator;

import com.senla.srs.dto.geo.CityDTO;
import com.senla.srs.dto.pointofrental.PointOfRentalRequestDTO;
import org.springframework.validation.Errors;

import java.util.Optional;

public interface PointOfRentalRequestValidator {

    PointOfRentalRequestDTO validate(PointOfRentalRequestDTO pointOfRentalRequestDTO,
                                     Optional<CityDTO> optionalCityDTO,
                                     Errors errors);

}
