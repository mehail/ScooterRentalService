package com.senla.srs.core.validator;

import com.senla.srs.core.dto.scooter.type.MakerDTO;
import com.senla.srs.core.dto.scooter.type.ScooterTypeRequestDTO;
import com.senla.srs.core.service.MakerDtoService;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Service("scooterTypeRequestValidator")
public class ScooterTypeRequestValidator implements Validator {

    private final MakerDtoService makerDtoService;

    public ScooterTypeRequestValidator(MakerDtoService makerDtoService) {
        this.makerDtoService = makerDtoService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return ScooterTypeRequestDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        var scooterTypeRequestDTO = (ScooterTypeRequestDTO) o;
        Optional<MakerDTO> optionalMakerDTO = makerDtoService.retrieveMakerDtoById(scooterTypeRequestDTO.getMakerId());

        if (optionalMakerDTO.isEmpty()) {
            errors.reject("makerId", "Maker with this ID does not exist");
        }

    }
}
