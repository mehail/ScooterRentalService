package com.senla.srs.service;

import com.senla.srs.dto.scooter.type.MakerDTO;

import java.util.Optional;

public interface MakerDtoService {

    Optional<MakerDTO> retrieveMakerDtoById(Long id);

}
