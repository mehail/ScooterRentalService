package com.senla.srs.mapper;

import com.senla.srs.dto.pointofrental.PointOfRentalRequestDTO;
import com.senla.srs.model.PointOfRental;
import org.springframework.stereotype.Component;

@Component
public class PointOfRentalRequestMapper extends AbstractMapper<PointOfRental, PointOfRentalRequestDTO> {
    public PointOfRentalRequestMapper() {
        super(PointOfRental.class, PointOfRentalRequestDTO.class);
    }
}
