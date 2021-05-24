package com.senla.srs.service;

import com.senla.srs.dto.geo.CityDTO;

import java.util.Optional;

public interface CityDtoService {

    Optional<CityDTO> retrieveGisPointDtoById(Long id);

}
