package com.senla.srs.mapper;

import com.senla.srs.dto.pointofrental.PointOfRentalRequestDTO;
import com.senla.srs.model.PointOfRental;
import com.senla.srs.service.AddressDtoService;
import org.springframework.stereotype.Component;

@Component
public class PointOfRentalRequestMapper extends AbstractMapper<PointOfRental, PointOfRentalRequestDTO> {
    private final AddressDtoService addressDtoService;

    public PointOfRentalRequestMapper(AddressDtoService addressDtoService) {
        super(PointOfRental.class, PointOfRentalRequestDTO.class);
        this.addressDtoService = addressDtoService;
    }

    @Override
    public PointOfRental toEntity(PointOfRentalRequestDTO dto) {
        PointOfRental pointOfRental = super.toEntity(dto);
        pointOfRental.setAddress(addressDtoService.retrieveAddressDtoById(dto.getAddressId()).get());
        return pointOfRental;
    }
}
