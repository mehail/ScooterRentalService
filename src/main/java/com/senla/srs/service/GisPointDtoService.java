package com.senla.srs.service;

import com.senla.srs.dto.geo.GisPointDTO;

import java.util.Optional;

public interface GisPointDtoService {

    Optional<GisPointDTO> retrieveGisPointDtoById(Long id);

}
