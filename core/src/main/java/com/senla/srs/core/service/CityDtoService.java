package com.senla.srs.core.service;

import com.senla.srs.core.dto.geo.CityDTO;

import java.util.Optional;

public interface CityDtoService {

    Optional<CityDTO> retrieveGisPointDtoById(Long id);

}
