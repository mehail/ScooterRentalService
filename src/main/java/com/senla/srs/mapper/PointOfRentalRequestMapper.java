package com.senla.srs.mapper;

import com.senla.srs.dto.db.AddressDTO;
import com.senla.srs.dto.pointofrental.PointOfRentalRequestDTO;
import com.senla.srs.entity.PointOfRental;
import org.springframework.stereotype.Component;

@Component
public class PointOfRentalRequestMapper extends AbstractMapper<PointOfRental, PointOfRentalRequestDTO> {

    public PointOfRentalRequestMapper() {
        super(PointOfRental.class, PointOfRentalRequestDTO.class);
    }

    public PointOfRental toEntity(PointOfRentalRequestDTO dto, AddressDTO addressDTO) {
        var pointOfRental = super.toEntity(dto);
        pointOfRental.setAddress(addressDTO);

        return pointOfRental;
    }

    public PointOfRental toEntity(PointOfRentalRequestDTO dto, AddressDTO addressDTO, Long id) {
        var pointOfRental = toEntity(dto, addressDTO);
        pointOfRental.setId(id);

        return pointOfRental;
    }

}
