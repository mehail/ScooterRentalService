package com.senla.srs.web.controller.v1.facade;

import com.senla.srs.core.dto.scooter.ScooterCompactResponseDTO;
import com.senla.srs.core.dto.scooter.ScooterDTO;
import com.senla.srs.core.dto.scooter.ScooterRequestDTO;
import com.senla.srs.core.entity.PointOfRental;
import com.senla.srs.core.entity.RentalSession;
import com.senla.srs.core.entity.Scooter;
import com.senla.srs.core.entity.ScooterType;
import com.senla.srs.core.exception.NotFoundEntityException;
import com.senla.srs.core.mapper.ScooterCompactResponseMapper;
import com.senla.srs.core.mapper.ScooterFullResponseMapper;
import com.senla.srs.core.mapper.ScooterRequestMapper;
import com.senla.srs.core.security.JwtTokenData;
import com.senla.srs.core.service.PointOfRentalService;
import com.senla.srs.core.service.RentalSessionService;
import com.senla.srs.core.service.ScooterService;
import com.senla.srs.core.service.ScooterTypeService;
import com.senla.srs.core.validatorOld.ScooterRequestValidator;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

@Controller
public class ScooterControllerFacade extends AbstractFacade implements
        EntityControllerFacade<ScooterDTO, ScooterRequestDTO, ScooterCompactResponseDTO, String> {

    private final PointOfRentalService pointOfRentalService;
    private final RentalSessionService rentalSessionService;
    private final ScooterService scooterService;
    private final ScooterTypeService scooterTypeService;
    private final ScooterRequestMapper scooterRequestMapper;
    private final ScooterCompactResponseMapper scooterCompactResponseMapper;
    private final ScooterFullResponseMapper scooterFullResponseMapper;
    private final ScooterRequestValidator scooterRequestValidator;

    public ScooterControllerFacade(PointOfRentalService pointOfRentalService,
                                   RentalSessionService rentalSessionService,
                                   ScooterService scooterService,
                                   ScooterTypeService scooterTypeService,
                                   ScooterRequestMapper scooterRequestMapper,
                                   ScooterCompactResponseMapper scooterCompactResponseMapper,
                                   ScooterFullResponseMapper scooterFullResponseMapper,
                                   ScooterRequestValidator scooterRequestValidator,
                                   JwtTokenData jwtTokenData) {
        super(jwtTokenData);
        this.pointOfRentalService = pointOfRentalService;
        this.rentalSessionService = rentalSessionService;
        this.scooterService = scooterService;
        this.scooterTypeService = scooterTypeService;
        this.scooterRequestMapper = scooterRequestMapper;
        this.scooterCompactResponseMapper = scooterCompactResponseMapper;
        this.scooterFullResponseMapper = scooterFullResponseMapper;
        this.scooterRequestValidator = scooterRequestValidator;
    }

    @Override
    public Page<ScooterCompactResponseDTO> getAll(Integer page, Integer size, String sort, String token) {
        return scooterCompactResponseMapper.mapPageToDtoPage(scooterService.retrieveAllScooters(page, size, sort));
    }

    @Override
    public ResponseEntity<?> getById(String serialNumber, String token) throws NotFoundEntityException {
        List<RentalSession> rentalSessions = rentalSessionService.retrieveRentalSessionByScooterSerialNumber(serialNumber);

        return new ResponseEntity<>(scooterService.retrieveScooterBySerialNumber(serialNumber)
                .map(isAdmin(token)
                        ? scooter -> scooterFullResponseMapper.toFullDto(scooter, rentalSessions)
                        : scooterCompactResponseMapper::toDto)
                .orElseThrow(() -> new NotFoundEntityException(Scooter.class, serialNumber)), HttpStatus.OK);
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

        var validScooterRequestDTO = scooterRequestValidator.validate(scooterRequestDTO,
                optionalPointOfRental, optionalScooterType, bindingResult);

        return save(validScooterRequestDTO, optionalScooterType, bindingResult);
    }

    @Override
    public ResponseEntity<?> delete(String serialNumber, String token) {
        scooterService.deleteById(serialNumber);

        return new ResponseEntity<>("Scooter with this serial number was deleted", HttpStatus.ACCEPTED);
    }

    @Override
    public String getExistEntityId(ScooterRequestDTO dto) {
        return dto.getSerialNumber();
    }

    private ResponseEntity<?> save(ScooterRequestDTO scooterRequestDTO,
                                   Optional<ScooterType> optionalScooterType,
                                   BindingResult bindingResult) {

        if (!bindingResult.hasErrors() && optionalScooterType.isPresent()) {
            var scooter = scooterService.save(scooterRequestMapper.toEntity(scooterRequestDTO, optionalScooterType.get()));

            return new ResponseEntity<>(scooterCompactResponseMapper.toDto(scooter), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

    }

}
