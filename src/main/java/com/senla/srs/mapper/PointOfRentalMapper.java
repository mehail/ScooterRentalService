package com.senla.srs.mapper;

import com.senla.srs.dto.PointOfRentalResponseDTO;
import com.senla.srs.model.PointOfRental;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PointOfRentalMapper extends AbstractMapper<PointOfRental, PointOfRentalResponseDTO> {

    @Autowired
    public PointOfRentalMapper() {
        super(PointOfRental.class, PointOfRentalResponseDTO.class);
    }

}
