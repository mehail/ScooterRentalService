package com.senla.srs.controller.v1.facade;

import com.senla.srs.dto.pointofrental.PointOfRentalDTO;
import com.senla.srs.dto.pointofrental.PointOfRentalRequestDTO;
import com.senla.srs.dto.pointofrental.PointOfRentalResponseDTO;
import com.senla.srs.exception.NotFoundEntityException;
import com.senla.srs.mapper.PointOfRentalRequestMapper;
import com.senla.srs.mapper.PointOfRentalResponseMapper;
import com.senla.srs.model.PointOfRental;
import com.senla.srs.service.AddressDtoService;
import com.senla.srs.service.PointOfRentalService;
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
public class PointOfRentalControllerFacade extends AbstractFacade implements
        EntityControllerFacade<PointOfRentalDTO, PointOfRentalRequestDTO, PointOfRentalResponseDTO, Long> {

    private static final String POINT_OF_RENTAL_NOT_FOUND = "A Point of rental with this id was not found";
    private final AddressDtoService addressDtoService;
    private final PointOfRentalService pointOfRentalService;
    private final PointOfRentalRequestMapper pointOfRentalRequestMapper;
    private final PointOfRentalResponseMapper pointOfRentalResponseMapper;

    public PointOfRentalControllerFacade(UserService userService, AddressDtoService addressDtoService, PointOfRentalService pointOfRentalService, PointOfRentalRequestMapper pointOfRentalRequestMapper, PointOfRentalResponseMapper pointOfRentalResponseMapper) {
        super(userService);
        this.addressDtoService = addressDtoService;
        this.pointOfRentalService = pointOfRentalService;
        this.pointOfRentalRequestMapper = pointOfRentalRequestMapper;
        this.pointOfRentalResponseMapper = pointOfRentalResponseMapper;
    }

    @Override
    public Page<PointOfRentalResponseDTO> getAll(Integer page, Integer size, String sort, User userSecurity) {
        return pointOfRentalResponseMapper.mapPageToDtoPage(pointOfRentalService.retrieveAllPointOfRentals(page, size, sort));
    }

    @Override
    public ResponseEntity<?> getById(Long id, User userSecurity) throws NotFoundEntityException {
        return new ResponseEntity<>(pointOfRentalService.retrievePointOfRentalById(id)
                .map(pointOfRentalResponseMapper::toDto)
                .orElseThrow(() -> new NotFoundEntityException("Point of rental")), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> createOrUpdate(PointOfRentalRequestDTO pointOfRentalRequestDTO, User userSecurity) {
        if (addressDtoService.retrieveAddressDtoById(pointOfRentalRequestDTO.getAddressId()).isEmpty()) {
            return new ResponseEntity<>("The address is not correct", HttpStatus.FORBIDDEN);
        }

        PointOfRental pointOfRental = pointOfRentalService.save(pointOfRentalRequestMapper.toEntity(pointOfRentalRequestDTO));

        return ResponseEntity.ok(pointOfRentalResponseMapper.toDto(pointOfRental));
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        try {
            pointOfRentalService.deleteById(id);
            return new ResponseEntity<>("Point of rental with this id was deleted", HttpStatus.ACCEPTED);
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage(), POINT_OF_RENTAL_NOT_FOUND);
            return new ResponseEntity<>(POINT_OF_RENTAL_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }
}
