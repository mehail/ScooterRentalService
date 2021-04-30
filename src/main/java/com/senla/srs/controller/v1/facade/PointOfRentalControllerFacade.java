package com.senla.srs.controller.v1.facade;

import com.senla.srs.dto.db.AddressDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

@Slf4j
@Controller
public class PointOfRentalControllerFacade extends AbstractFacade implements
        EntityControllerFacade<PointOfRentalDTO, PointOfRentalRequestDTO, PointOfRentalResponseDTO, Long> {

    private final AddressDtoService addressDtoService;
    private final PointOfRentalService pointOfRentalService;
    private final PointOfRentalRequestMapper pointOfRentalRequestMapper;
    private final PointOfRentalResponseMapper pointOfRentalResponseMapper;

    public PointOfRentalControllerFacade(UserService userService,
                                         AddressDtoService addressDtoService,
                                         PointOfRentalService pointOfRentalService,
                                         PointOfRentalRequestMapper pointOfRentalRequestMapper,
                                         PointOfRentalResponseMapper pointOfRentalResponseMapper) {
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
    public ResponseEntity<?> createOrUpdate(PointOfRentalRequestDTO pointOfRentalRequestDTO,
                                            BindingResult bindingResult,
                                            User userSecurity)
            throws NotFoundEntityException {

        if (addressDtoService.retrieveAddressDtoById(pointOfRentalRequestDTO.getAddressId()).isEmpty()) {
            return new ResponseEntity<>("The address is not correct", HttpStatus.FORBIDDEN);
        }

        AddressDTO addressDTO = addressDtoService.retrieveAddressDtoById(pointOfRentalRequestDTO.getAddressId())
                .orElseThrow(() -> new NotFoundEntityException("Address"));

        PointOfRental pointOfRental = pointOfRentalService.save(pointOfRentalRequestMapper.toEntity(pointOfRentalRequestDTO, addressDTO));

        return ResponseEntity.ok(pointOfRentalResponseMapper.toDto(pointOfRental));
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        pointOfRentalService.deleteById(id);
        return new ResponseEntity<>("Point of rental with this id was deleted", HttpStatus.ACCEPTED);
    }
}
