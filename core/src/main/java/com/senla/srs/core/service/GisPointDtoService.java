package com.senla.srs.core.service;

import com.senla.srs.core.dto.geo.GisPointDTO;

import java.util.Optional;

public interface GisPointDtoService {

    Optional<GisPointDTO> retrieveGisPointDtoById(Long id);

}
