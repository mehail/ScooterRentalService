package com.senla.srs.service;

import com.senla.srs.dto.db.MakerDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface MakerDtoService {
    Optional<MakerDTO> retrieveMakerDtoById(Long id);
}
