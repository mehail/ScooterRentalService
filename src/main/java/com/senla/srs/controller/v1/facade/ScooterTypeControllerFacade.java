package com.senla.srs.controller.v1.facade;

import com.senla.srs.dto.db.MakerDTO;
import com.senla.srs.dto.scooter.type.ScooterTypeDTO;
import com.senla.srs.dto.scooter.type.ScooterTypeRequestDTO;
import com.senla.srs.dto.scooter.type.ScooterTypeResponseDTO;
import com.senla.srs.entity.ScooterType;
import com.senla.srs.exception.NotFoundEntityException;
import com.senla.srs.mapper.ScooterTypeRequestMapper;
import com.senla.srs.mapper.ScooterTypeResponseMapper;
import com.senla.srs.security.JwtTokenData;
import com.senla.srs.service.MakerDtoService;
import com.senla.srs.service.ScooterTypeService;
import com.senla.srs.validator.ScooterTypeRequestValidator;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import java.util.Optional;

@Controller
public class ScooterTypeControllerFacade extends AbstractFacade implements
        EntityControllerFacade<ScooterTypeDTO, ScooterTypeRequestDTO, ScooterTypeResponseDTO, Long> {

    private final ScooterTypeService scooterTypeService;
    private final MakerDtoService makerDtoService;
    private final ScooterTypeRequestMapper scooterTypeRequestMapper;
    private final ScooterTypeResponseMapper scooterTypeResponseMapper;
    private final ScooterTypeRequestValidator scooterTypeRequestValidator;

    public ScooterTypeControllerFacade(ScooterTypeService scooterTypeService,
                                       MakerDtoService makerDtoService,
                                       ScooterTypeRequestMapper scooterTypeRequestMapper,
                                       ScooterTypeResponseMapper scooterTypeResponseMapper,
                                       ScooterTypeRequestValidator scooterTypeRequestValidator,
                                       JwtTokenData jwtTokenData) {
        super(jwtTokenData);
        this.scooterTypeService = scooterTypeService;
        this.makerDtoService = makerDtoService;
        this.scooterTypeRequestMapper = scooterTypeRequestMapper;
        this.scooterTypeResponseMapper = scooterTypeResponseMapper;
        this.scooterTypeRequestValidator = scooterTypeRequestValidator;
    }

    @Override
    public Page<ScooterTypeResponseDTO> getAll(Integer page, Integer size, String sort, String token) {
        return scooterTypeResponseMapper.mapPageToDtoPage(scooterTypeService.retrieveAllScooterTypes(page, size, sort));
    }

    @Override
    public ResponseEntity<?> getById(Long id, String token) throws NotFoundEntityException {
        return new ResponseEntity<>(scooterTypeService.retrieveScooterTypeById(id)
                .map(scooterTypeResponseMapper::toDto)
                .orElseThrow(() -> new NotFoundEntityException(ScooterType.class, id)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> createOrUpdate(ScooterTypeRequestDTO scooterTypeRequestDTO,
                                            BindingResult bindingResult,
                                            String token)
            throws NotFoundEntityException {

        Optional<MakerDTO> optionalMakerDTO = makerDtoService.retrieveMakerDtoById(scooterTypeRequestDTO.getMakerId());

        ScooterTypeRequestDTO validScooterTypeRequestDTO =
                scooterTypeRequestValidator.validate(scooterTypeRequestDTO, optionalMakerDTO, bindingResult);

        return save(validScooterTypeRequestDTO, optionalMakerDTO, bindingResult);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        scooterTypeService.deleteById(id);
        return new ResponseEntity<>("Scooter type with this id was deleted", HttpStatus.ACCEPTED);
    }

    private ResponseEntity<?> save(ScooterTypeRequestDTO scooterTypeRequestDTO,
                                   Optional<MakerDTO> optionalMakerDTO,
                                   BindingResult bindingResult) {

        if (!bindingResult.hasErrors() && optionalMakerDTO.isPresent()) {
            ScooterType scooterType = scooterTypeService.save(scooterTypeRequestMapper
                    .toEntity(scooterTypeRequestDTO, optionalMakerDTO.get()));

            return ResponseEntity.ok(scooterTypeResponseMapper.toDto(scooterType));
        } else {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

    }
}
