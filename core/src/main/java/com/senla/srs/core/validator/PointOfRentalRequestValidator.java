package com.senla.srs.core.validator;

import com.senla.srs.core.dto.pointofrental.PointOfRentalRequestDTO;
import com.senla.srs.core.service.CityDtoService;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service("pointOfRentalRequestValidator")
public class PointOfRentalRequestValidator implements Validator {

    private final CityDtoService cityDtoService;

    public PointOfRentalRequestValidator(CityDtoService cityDtoService) {
        this.cityDtoService = cityDtoService;
    }

    @Override
    public boolean supports(@NonNull Class<?> aClass) {
        return PointOfRentalRequestDTO.class.equals(aClass);
    }

    @Override
    public void validate(@NonNull Object o, @NonNull Errors errors) {

        PointOfRentalRequestDTO pointOfRentalRequestDTO = (PointOfRentalRequestDTO) o;

        if (cityDtoService.retrieveGisPointDtoById(pointOfRentalRequestDTO.getCityId()).isEmpty()) {
            errors.reject("cityId", "City with this ID does not exist");
        }

    }

}
