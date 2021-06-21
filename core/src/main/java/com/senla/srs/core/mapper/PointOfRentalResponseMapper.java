package com.senla.srs.core.mapper;

import com.senla.srs.core.dto.pointofrental.PointOfRentalResponseDTO;
import com.senla.srs.core.entity.PointOfRental;
import org.springframework.stereotype.Component;

@Component
public class PointOfRentalResponseMapper extends AbstractMapperWithPagination<PointOfRental, PointOfRentalResponseDTO> {

    public PointOfRentalResponseMapper() {
        super(PointOfRental.class, PointOfRentalResponseDTO.class);
    }

}
