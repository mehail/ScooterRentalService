package com.senla.srs.controller.v1.facade;

import com.senla.srs.dto.scooter.ScooterDTO;
import com.senla.srs.dto.scooter.ScooterRequestDTO;
import com.senla.srs.dto.scooter.ScooterResponseDTO;
import com.senla.srs.entity.PointOfRental;
import com.senla.srs.entity.Scooter;
import com.senla.srs.entity.ScooterType;
import com.senla.srs.exception.NotFoundEntityException;
import com.senla.srs.mapper.ScooterRequestMapper;
import com.senla.srs.mapper.ScooterResponseMapper;
import com.senla.srs.security.JwtTokenData;
import com.senla.srs.service.PointOfRentalService;
import com.senla.srs.service.ScooterService;
import com.senla.srs.service.ScooterTypeService;
import com.senla.srs.validator.ScooterRequestValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import java.util.Optional;

@Slf4j
@Controller
public class ScooterControllerFacade extends AbstractFacade implements
        EntityControllerFacade<ScooterDTO, ScooterRequestDTO, ScooterResponseDTO, String> {

    private final ScooterService scooterService;
    private final ScooterTypeService scooterTypeService;
    private final PointOfRentalService pointOfRentalService;
    private final ScooterRequestMapper scooterRequestMapper;
    private final ScooterResponseMapper scooterResponseMapper;
    private final ScooterRequestValidator scooterRequestValidator;

    public ScooterControllerFacade(ScooterService scooterService,
                                   ScooterTypeService scooterTypeService,
                                   ScooterRequestMapper scooterRequestMapper,
                                   ScooterResponseMapper scooterResponseMapper,
                                   PointOfRentalService pointOfRentalService,
                                   ScooterRequestValidator scooterRequestValidator,
                                   JwtTokenData jwtTokenData) {
        super(jwtTokenData);
        this.scooterService = scooterService;
        this.scooterTypeService = scooterTypeService;
        this.scooterRequestMapper = scooterRequestMapper;
        this.scooterResponseMapper = scooterResponseMapper;
        this.pointOfRentalService = pointOfRentalService;
        this.scooterRequestValidator = scooterRequestValidator;
    }

    @Override
    public Page<ScooterResponseDTO> getAll(Integer page, Integer size, String sort, String token) {
        return scooterResponseMapper.mapPageToDtoPage(scooterService.retrieveAllScooters(page, size, sort));
    }

    @Override
    public ResponseEntity<?> getById(String serialNumber, String token) throws NotFoundEntityException {
        return new ResponseEntity<>(scooterService.retrieveScooterBySerialNumber(serialNumber)
                .map(scooterResponseMapper::toDto)
                .orElseThrow(() -> new NotFoundEntityException("Scooter")), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> createOrUpdate(ScooterRequestDTO scooterRequestDTO,
                                            BindingResult bindingResult,
                                            String token)
            throws NotFoundEntityException {


        Optional<PointOfRental> optionalPointOfRental =
                pointOfRentalService.retrievePointOfRentalById(scooterRequestDTO.getPointOfRentalId());
        Optional<ScooterType> optionalScooterType =
                scooterTypeService.retrieveScooterTypeById(scooterRequestDTO.getTypeId());

        ScooterRequestDTO validScooterRequestDTO =
                scooterRequestValidator.validate(scooterRequestDTO, optionalPointOfRental, optionalScooterType, bindingResult);

        return save(validScooterRequestDTO, optionalScooterType, bindingResult);
    }

    @Override
    public ResponseEntity<?> delete(String serialNumber) {
        scooterService.deleteById(serialNumber);
        return new ResponseEntity<>("Scooter with this serial number was deleted", HttpStatus.ACCEPTED);
    }

    private ResponseEntity<?> save(ScooterRequestDTO scooterRequestDTO,
                                   Optional<ScooterType> optionalScooterType,
                                   BindingResult bindingResult) {

        if (!bindingResult.hasErrors() && optionalScooterType.isPresent()) {
            Scooter scooter =
                    scooterService.save(scooterRequestMapper.toEntity(scooterRequestDTO, optionalScooterType.get()));

            return ResponseEntity.ok(scooterResponseMapper.toDto(scooter));
        } else {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

    }
}
