package com.senla.srs.core.validator;

import com.senla.srs.core.dto.scooter.ScooterRequestDTO;
import com.senla.srs.core.service.PointOfRentalService;
import com.senla.srs.core.service.ScooterTypeService;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service("scooterRequestValidator")
public class ScooterRequestValidator implements Validator {

    private final PointOfRentalService pointOfRentalService;
    private final ScooterTypeService scooterTypeService;

    public ScooterRequestValidator(PointOfRentalService pointOfRentalService, ScooterTypeService scooterTypeService) {
        this.pointOfRentalService = pointOfRentalService;
        this.scooterTypeService = scooterTypeService;
    }

    @Override
    public boolean supports(@NonNull Class<?> aClass) {
        return ScooterRequestDTO.class.equals(aClass);
    }

    @Override
    public void validate(@NonNull Object o, @NonNull Errors errors) {

        var scooterRequestDTO = (ScooterRequestDTO) o;

        if (pointOfRentalService.retrievePointOfRentalById(scooterRequestDTO.getPointOfRentalId()).isEmpty()) {
            errors.reject("pointOfRentalId", "Point of rentalId with this ID does not exist");
        }

        if (scooterTypeService.retrieveScooterTypeById(scooterRequestDTO.getTypeId()).isEmpty()) {
            errors.reject("typeId", "Scooter type with this ID does not exist");
        }

    }

}
