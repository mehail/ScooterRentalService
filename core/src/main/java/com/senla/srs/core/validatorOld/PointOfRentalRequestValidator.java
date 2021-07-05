package com.senla.srs.core.validatorOld;

import com.senla.srs.core.dto.geo.CityDTO;
import com.senla.srs.core.dto.pointofrental.PointOfRentalRequestDTO;
import org.springframework.validation.Errors;

import java.util.Optional;

public interface PointOfRentalRequestValidator {

    PointOfRentalRequestDTO validate(PointOfRentalRequestDTO pointOfRentalRequestDTO,
                                     Optional<CityDTO> optionalCityDTO,
                                     Errors errors);

}
