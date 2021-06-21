package com.senla.srs.core.service.impl;

import com.senla.srs.core.dto.scooter.type.MakerDTO;
import com.senla.srs.core.repository.MakerDtoRepository;
import com.senla.srs.core.service.MakerDtoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class MakerDtoServiceImpl implements MakerDtoService {

    private final MakerDtoRepository makerDtoRepository;

    @Override
    public Optional<MakerDTO> retrieveMakerDtoById(Long id) {
        return makerDtoRepository.findById(id);
    }

}
