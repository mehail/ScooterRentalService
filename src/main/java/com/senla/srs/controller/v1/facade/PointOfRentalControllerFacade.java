package com.senla.srs.controller.v1.facade;

import com.senla.srs.dto.db.AddressDTO;
import com.senla.srs.dto.pointofrental.PointOfRentalDTO;
import com.senla.srs.dto.pointofrental.PointOfRentalRequestDTO;
import com.senla.srs.dto.pointofrental.PointOfRentalResponseDTO;
import com.senla.srs.entity.PointOfRental;
import com.senla.srs.exception.NotFoundEntityException;
import com.senla.srs.mapper.PointOfRentalRequestMapper;
import com.senla.srs.mapper.PointOfRentalResponseMapper;
import com.senla.srs.security.JwtTokenData;
import com.senla.srs.service.AddressDtoService;
import com.senla.srs.service.PointOfRentalService;
import com.senla.srs.validator.PointOfRentalRequestValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import java.util.Optional;

@Slf4j
@Controller
public class PointOfRentalControllerFacade extends AbstractFacade implements
        EntityControllerFacade<PointOfRentalDTO, PointOfRentalRequestDTO, PointOfRentalResponseDTO, Long> {

    private final AddressDtoService addressDtoService;
    private final PointOfRentalService pointOfRentalService;
    private final PointOfRentalRequestMapper pointOfRentalRequestMapper;
    private final PointOfRentalResponseMapper pointOfRentalResponseMapper;
    private final PointOfRentalRequestValidator pointOfRentalRequestValidator;

    public PointOfRentalControllerFacade(AddressDtoService addressDtoService,
                                         PointOfRentalService pointOfRentalService,
                                         PointOfRentalRequestMapper pointOfRentalRequestMapper,
                                         PointOfRentalResponseMapper pointOfRentalResponseMapper,
                                         PointOfRentalRequestValidator pointOfRentalRequestValidator,
                                         JwtTokenData jwtTokenData) {
        super(jwtTokenData);
        this.addressDtoService = addressDtoService;
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

        Optional<AddressDTO> optionalAddressDTO =
                addressDtoService.retrieveAddressDtoById(pointOfRentalRequestDTO.getAddressId());

        PointOfRentalRequestDTO validPointOfRentalRequestDTO =
                pointOfRentalRequestValidator.validate(pointOfRentalRequestDTO, optionalAddressDTO, bindingResult);

        return save(validPointOfRentalRequestDTO, optionalAddressDTO, bindingResult);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        pointOfRentalService.deleteById(id);
        return new ResponseEntity<>("Point of rental with this id was deleted", HttpStatus.ACCEPTED);
    }

    private ResponseEntity<?> save(PointOfRentalRequestDTO pointOfRentalRequestDTO,
                                   Optional<AddressDTO> optionalAddressDTO,
                                   BindingResult bindingResult) {

        if (!bindingResult.hasErrors() && optionalAddressDTO.isPresent()) {
            PointOfRental pointOfRental = pointOfRentalService.save(pointOfRentalRequestMapper
                            .toEntity(pointOfRentalRequestDTO, optionalAddressDTO.get()));

            return ResponseEntity.ok(pointOfRentalResponseMapper.toDto(pointOfRental));
        } else {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

    }

}
