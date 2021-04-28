package com.senla.srs.controller.v1.facade;

import com.senla.srs.dto.scooter.type.ScooterTypeDTO;
import com.senla.srs.dto.scooter.type.ScooterTypeRequestDTO;
import com.senla.srs.dto.scooter.type.ScooterTypeResponseDTO;
import com.senla.srs.exception.NotFoundEntityException;
import com.senla.srs.mapper.ScooterTypeRequestMapper;
import com.senla.srs.mapper.ScooterTypeResponseMapper;
import com.senla.srs.model.ScooterType;
import com.senla.srs.service.MakerDtoService;
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
public class ScooterTypeControllerFacade extends AbstractFacade implements
        EntityControllerFacade<ScooterTypeDTO, ScooterTypeRequestDTO, ScooterTypeResponseDTO, Long> {

    private static final String TYPE_NOT_FOUND = "Scooter type with this id not found";
    private final ScooterTypeService scooterTypeService;
    private final MakerDtoService makerDtoService;
    private final ScooterTypeRequestMapper scooterTypeRequestMapper;
    private final ScooterTypeResponseMapper scooterTypeResponseMapper;

    public ScooterTypeControllerFacade(UserService userService, ScooterTypeService scooterTypeService, MakerDtoService makerDtoService, ScooterTypeRequestMapper scooterTypeRequestMapper, ScooterTypeResponseMapper scooterTypeResponseMapper) {
        super(userService);
        this.scooterTypeService = scooterTypeService;
        this.makerDtoService = makerDtoService;
        this.scooterTypeRequestMapper = scooterTypeRequestMapper;
        this.scooterTypeResponseMapper = scooterTypeResponseMapper;
    }

    @Override
    public Page<ScooterTypeResponseDTO> getAll(Integer page, Integer size, String sort, User userSecurity) {
        return scooterTypeResponseMapper.mapPageToDtoPage(scooterTypeService.retrieveAllScooterTypes(page, size, sort));
    }

    @Override
    public ResponseEntity<?> getById(Long id, User userSecurity) throws NotFoundEntityException {
        return new ResponseEntity<>(scooterTypeService.retrieveScooterTypeById(id)
                .map(scooterTypeResponseMapper::toDto)
                .orElseThrow(() -> new NotFoundEntityException("Scooter type")), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> createOrUpdate(ScooterTypeRequestDTO requestDTO, User userSecurity) {
        if (makerDtoService.retrieveMakerDtoById(requestDTO.getMakerId()).isEmpty()) {
            return new ResponseEntity<>("The maker is not correct", HttpStatus.FORBIDDEN);
        }

        ScooterType scooterType = scooterTypeService.save(scooterTypeRequestMapper.toEntity(requestDTO));

        return ResponseEntity.ok(scooterTypeResponseMapper.toDto(scooterType));
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        try {
            scooterTypeService.deleteById(id);
            return new ResponseEntity<>("Scooter type with this id was deleted", HttpStatus.ACCEPTED);
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage(), TYPE_NOT_FOUND);
            return new ResponseEntity<>(TYPE_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }
}
