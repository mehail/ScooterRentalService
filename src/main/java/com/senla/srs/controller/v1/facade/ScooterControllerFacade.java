package com.senla.srs.controller.v1.facade;

import com.senla.srs.dto.scooter.ScooterDTO;
import com.senla.srs.dto.scooter.ScooterRequestDTO;
import com.senla.srs.dto.scooter.ScooterResponseDTO;
import com.senla.srs.entity.Scooter;
import com.senla.srs.entity.ScooterType;
import com.senla.srs.exception.NotFoundEntityException;
import com.senla.srs.mapper.ScooterRequestMapper;
import com.senla.srs.mapper.ScooterResponseMapper;
import com.senla.srs.security.JwtTokenData;
import com.senla.srs.service.ScooterService;
import com.senla.srs.service.ScooterTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

@Slf4j
@Controller
public class ScooterControllerFacade extends AbstractFacade implements
        EntityControllerFacade<ScooterDTO, ScooterRequestDTO, ScooterResponseDTO, String> {

    private final ScooterService scooterService;
    private final ScooterTypeService scooterTypeService;
    private final ScooterRequestMapper scooterRequestMapper;
    private final ScooterResponseMapper scooterResponseMapper;

    public ScooterControllerFacade(ScooterService scooterService,
                                   ScooterTypeService scooterTypeService,
                                   ScooterRequestMapper scooterRequestMapper,
                                   ScooterResponseMapper scooterResponseMapper,
                                   JwtTokenData jwtTokenData) {
        super(jwtTokenData);
        this.scooterService = scooterService;
        this.scooterTypeService = scooterTypeService;
        this.scooterRequestMapper = scooterRequestMapper;
        this.scooterResponseMapper = scooterResponseMapper;
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
    public ResponseEntity<?> createOrUpdate(ScooterRequestDTO scooterDTO,
                                            BindingResult bindingResult,
                                            String token)
            throws NotFoundEntityException {

        ScooterType scooterType = scooterTypeService.retrieveScooterTypeById(scooterDTO.getPointOfRentalId())
                .orElseThrow(() -> new NotFoundEntityException("Scooter type"));

        Scooter scooter = scooterService.save(scooterRequestMapper.toEntity(scooterDTO, scooterType));

        return ResponseEntity.ok(scooterResponseMapper.toDto(scooter));
    }

    @Override
    public ResponseEntity<?> delete(String serialNumber) {
        scooterService.deleteById(serialNumber);
        return new ResponseEntity<>("Scooter with this serial number was deleted", HttpStatus.ACCEPTED);
    }
}
