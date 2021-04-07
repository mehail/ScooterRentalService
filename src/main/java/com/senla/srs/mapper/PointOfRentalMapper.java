package com.senla.srs.mapper;

import com.senla.srs.dto.pointofrental.PointOfRentalDTO;
import com.senla.srs.model.PointOfRental;
import org.springframework.stereotype.Component;

@Component
public class PointOfRentalMapper extends AbstractMapper<PointOfRental, PointOfRentalDTO> {
    public PointOfRentalMapper() {
        super(PointOfRental.class, PointOfRentalDTO.class);
    }
}
