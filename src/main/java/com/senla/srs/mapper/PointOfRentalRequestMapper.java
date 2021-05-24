package com.senla.srs.mapper;

import com.senla.srs.dto.geo.CityDTO;
import com.senla.srs.dto.pointofrental.PointOfRentalRequestDTO;
import com.senla.srs.entity.PointOfRental;
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
