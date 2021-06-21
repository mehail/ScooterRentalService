package com.senla.srs.core.mapper;

import com.senla.srs.core.dto.geo.CityDTO;
import com.senla.srs.core.dto.pointofrental.PointOfRentalRequestDTO;
import com.senla.srs.core.entity.PointOfRental;
import org.springframework.stereotype.Component;

@Component
public class PointOfRentalRequestMapper extends AbstractMapper<PointOfRental, PointOfRentalRequestDTO> {

    public PointOfRentalRequestMapper() {
        super(PointOfRental.class, PointOfRentalRequestDTO.class);
    }

    public PointOfRental toEntity(PointOfRentalRequestDTO dto, CityDTO cityDTO) {
        var pointOfRental = super.toEntity(dto);
        pointOfRental.setCityDTO(cityDTO);

        return pointOfRental;
    }

    public PointOfRental toEntity(PointOfRentalRequestDTO dto, CityDTO cityDTO, Long id) {
        var pointOfRental = toEntity(dto, cityDTO);
        pointOfRental.setId(id);

        return pointOfRental;
    }

}
