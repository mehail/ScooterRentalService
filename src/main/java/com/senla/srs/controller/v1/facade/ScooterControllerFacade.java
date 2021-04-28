package com.senla.srs.controller.v1.facade;

import com.senla.srs.dto.scooter.ScooterDTO;
import com.senla.srs.dto.scooter.ScooterRequestDTO;
import com.senla.srs.dto.scooter.ScooterResponseDTO;
import com.senla.srs.exception.NotFoundEntityException;
import com.senla.srs.mapper.ScooterRequestMapper;
import com.senla.srs.mapper.ScooterResponseMapper;
import com.senla.srs.model.Scooter;
import com.senla.srs.model.ScooterType;
import com.senla.srs.service.ScooterService;
import com.senla.srs.service.ScooterTypeService;
import com.senla.srs.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class ScooterControllerFacade extends AbstractFacade implements
        EntityControllerFacade<ScooterDTO, ScooterRequestDTO, ScooterResponseDTO, String> {

    private static final String SCOOTER_NOT_FOUND = "A scooter with this serial number was not found";
    private final ScooterService scooterService;
    private final ScooterTypeService scooterTypeService;
    private final ScooterRequestMapper scooterRequestMapper;
    private final ScooterResponseMapper scooterResponseMapper;

    public ScooterControllerFacade(UserService userService,
                                   ScooterService scooterService,
                                   ScooterTypeService scooterTypeService,
                                   ScooterRequestMapper scooterRequestMapper,
                                   ScooterResponseMapper scooterResponseMapper) {
        super(userService);
        this.scooterService = scooterService;
        this.scooterTypeService = scooterTypeService;
        this.scooterRequestMapper = scooterRequestMapper;
        this.scooterResponseMapper = scooterResponseMapper;
    }

    @Override
    public Page<ScooterResponseDTO> getAll(Integer page, Integer size, String sort, User userSecurity) {
        return scooterResponseMapper.mapPageToDtoPage(scooterService.retrieveAllScooters(page, size, sort));
    }

    @Override
    public ResponseEntity<?> getById(String serialNumber, User userSecurity) throws NotFoundEntityException {
        return new ResponseEntity<>(scooterService.retrieveScooterBySerialNumber(serialNumber)
                .map(scooterResponseMapper::toDto)
                .orElseThrow(() -> new NotFoundEntityException("Scooter")), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> createOrUpdate(ScooterRequestDTO scooterDTO, User userSecurity) throws NotFoundEntityException {
        ScooterType scooterType =  scooterTypeService.retrieveScooterTypeById(scooterDTO.getPointOfRentalId())
                .orElseThrow(() -> new NotFoundEntityException("Scooter type"));

        Scooter scooter = scooterService.save(scooterRequestMapper.toEntity(scooterDTO, scooterType));

        return ResponseEntity.ok(scooterResponseMapper.toDto(scooter));
    }

    @Override
    public ResponseEntity<?> delete(String serialNumber) {
        try {
            scooterService.deleteById(serialNumber);
            return new ResponseEntity<>("Scooter with this serial number was deleted", HttpStatus.ACCEPTED);
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage(), SCOOTER_NOT_FOUND);
            return new ResponseEntity<>(SCOOTER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }
}
