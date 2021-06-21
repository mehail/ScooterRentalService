package com.senla.srs.core.service;

import com.senla.srs.core.dto.scooter.type.MakerDTO;

import java.util.Optional;

public interface MakerDtoService {

    Optional<MakerDTO> retrieveMakerDtoById(Long id);

}
