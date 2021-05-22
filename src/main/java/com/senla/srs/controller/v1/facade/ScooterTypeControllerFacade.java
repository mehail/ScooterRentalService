package com.senla.srs.controller.v1.facade;

import com.senla.srs.dto.scooter.type.MakerDTO;
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

    private final MakerDtoService makerDtoService;
    private final ScooterTypeService scooterTypeService;
    private final ScooterTypeRequestMapper scooterTypeRequestMapper;
    private final ScooterTypeResponseMapper scooterTypeResponseMapper;
    private final ScooterTypeRequestValidator scooterTypeRequestValidator;

    public ScooterTypeControllerFacade(MakerDtoService makerDtoService,
                                       ScooterTypeService scooterTypeService,
                                       ScooterTypeRequestMapper scooterTypeRequestMapper,
                                       ScooterTypeResponseMapper scooterTypeResponseMapper,
                                       ScooterTypeRequestValidator scooterTypeRequestValidator,
                                       JwtTokenData jwtTokenData) {
        super(jwtTokenData);
        this.makerDtoService = makerDtoService;
        this.scooterTypeService = scooterTypeService;
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

        var validScooterTypeRequestDTO = scooterTypeRequestValidator.validate(scooterTypeRequestDTO, optionalMakerDTO, bindingResult);

        return save(validScooterTypeRequestDTO, optionalMakerDTO, bindingResult);
    }

    @Override
    public ResponseEntity<?> delete(Long id, String token) {
        scooterTypeService.deleteById(id);

        return new ResponseEntity<>("Scooter type with this id was deleted", HttpStatus.ACCEPTED);
    }

    @Override
    public Long getExistEntityId(ScooterTypeRequestDTO dto) {
        return scooterTypeService.retrieveScooterTypeByModel(dto.getModel())
                .map(ScooterType::getId)
                .orElse(null);
    }

    private ResponseEntity<?> save(ScooterTypeRequestDTO scooterTypeRequestDTO,
                                   Optional<MakerDTO> optionalMakerDTO,
                                   BindingResult bindingResult) {

        if (!bindingResult.hasErrors() && optionalMakerDTO.isPresent()) {
            var scooterType = scooterTypeService.save(scooterTypeRequestMapper
                    .toEntity(scooterTypeRequestDTO, optionalMakerDTO.get(), getExistEntityId(scooterTypeRequestDTO)));

            return new ResponseEntity<>(scooterTypeResponseMapper.toDto(scooterType), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

    }

}
