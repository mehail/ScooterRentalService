package com.senla.srs.web.controller.v1.facade;

import com.senla.srs.core.dto.geo.CityDTO;
import com.senla.srs.core.dto.pointofrental.PointOfRentalDTO;
import com.senla.srs.core.dto.pointofrental.PointOfRentalRequestDTO;
import com.senla.srs.core.dto.pointofrental.PointOfRentalResponseDTO;
import com.senla.srs.core.entity.PointOfRental;
import com.senla.srs.core.exception.NotFoundEntityException;
import com.senla.srs.core.mapper.PointOfRentalRequestMapper;
import com.senla.srs.core.mapper.PointOfRentalResponseMapper;
import com.senla.srs.core.security.JwtTokenData;
import com.senla.srs.core.service.CityDtoService;
import com.senla.srs.core.service.PointOfRentalService;
import com.senla.srs.core.validatorOld.PointOfRentalRequestValidator;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import java.util.Optional;

@Controller
public class PointOfRentalControllerFacade extends AbstractFacade implements
        EntityControllerFacade<PointOfRentalDTO, PointOfRentalRequestDTO, PointOfRentalResponseDTO, Long> {

    private final CityDtoService cityDtoService;
    private final PointOfRentalService pointOfRentalService;
    private final PointOfRentalRequestMapper pointOfRentalRequestMapper;
    private final PointOfRentalResponseMapper pointOfRentalResponseMapper;
    private final PointOfRentalRequestValidator pointOfRentalRequestValidator;

    public PointOfRentalControllerFacade(CityDtoService cityDtoService,
                                         PointOfRentalService pointOfRentalService,
                                         PointOfRentalRequestMapper pointOfRentalRequestMapper,
                                         PointOfRentalResponseMapper pointOfRentalResponseMapper,
                                         PointOfRentalRequestValidator pointOfRentalRequestValidator,
                                         JwtTokenData jwtTokenData) {
        super(jwtTokenData);
        this.cityDtoService = cityDtoService;
        this.pointOfRentalService = pointOfRentalService;
        this.pointOfRentalRequestMapper = pointOfRentalRequestMapper;
        this.pointOfRentalResponseMapper = pointOfRentalResponseMapper;
        this.pointOfRentalRequestValidator = pointOfRentalRequestValidator;
    }


    @Override
    public Page<PointOfRentalResponseDTO> getAll(Integer page, Integer size, String sort, String token) {
        return pointOfRentalResponseMapper.mapPageToDtoPage(pointOfRentalService.retrieveAllPointOfRentals(page, size, sort));
    }

    @Override
    public ResponseEntity<?> getById(Long id, String token) throws NotFoundEntityException {
        return new ResponseEntity<>(pointOfRentalService.retrievePointOfRentalById(id)
                .map(pointOfRentalResponseMapper::toDto)
                .orElseThrow(() -> new NotFoundEntityException(PointOfRentalRequestDTO.class, id)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> createOrUpdate(PointOfRentalRequestDTO pointOfRentalRequestDTO,
                                            BindingResult bindingResult,
                                            String token)
            throws NotFoundEntityException {

        Optional<CityDTO> optionalCityDTO =
                cityDtoService.retrieveGisPointDtoById(pointOfRentalRequestDTO.getCityId());

        var validPointOfRentalRequestDTO =
                pointOfRentalRequestValidator.validate(pointOfRentalRequestDTO, optionalCityDTO, bindingResult);

        return save(validPointOfRentalRequestDTO, optionalCityDTO, bindingResult);
    }

    @Override
    public ResponseEntity<?> delete(Long id, String token) {
        pointOfRentalService.deleteById(id);

        return new ResponseEntity<>("Point of rental with this id was deleted", HttpStatus.ACCEPTED);
    }

    @Override
    public Long getExistEntityId(PointOfRentalRequestDTO dto) {
        return pointOfRentalService.retrievePointOfRentalByName(dto.getName())
                .map(PointOfRental::getId)
                .orElse(null);
    }

    private ResponseEntity<?> save(PointOfRentalRequestDTO pointOfRentalRequestDTO,
                                   Optional<CityDTO> optionalCityDTO,
                                   BindingResult bindingResult) {

        if (!bindingResult.hasErrors() && optionalCityDTO.isPresent()) {
            var pointOfRental = pointOfRentalService.save(pointOfRentalRequestMapper
                    .toEntity(pointOfRentalRequestDTO,
                            optionalCityDTO.get(),
                            getExistEntityId(pointOfRentalRequestDTO)));

            return new ResponseEntity<>(pointOfRentalResponseMapper.toDto(pointOfRental), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

    }

}
