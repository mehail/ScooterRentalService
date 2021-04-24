package com.senla.srs.mapper;

import com.senla.srs.dto.pointofrental.PointOfRentalResponseDTO;
import com.senla.srs.model.PointOfRental;
import org.springframework.stereotype.Component;

@Component
public class PointOfRentalResponseMapper extends AbstractMapperWithPagination<PointOfRental, PointOfRentalResponseDTO> {
    public PointOfRentalResponseMapper() {
        super(PointOfRental.class, PointOfRentalResponseDTO.class);
    }
}
