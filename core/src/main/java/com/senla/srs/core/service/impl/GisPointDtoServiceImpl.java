package com.senla.srs.core.service.impl;

import com.senla.srs.core.dto.geo.GisPointDTO;
import com.senla.srs.core.repository.GisPointDtoRepository;
import com.senla.srs.core.service.GisPointDtoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class GisPointDtoServiceImpl implements GisPointDtoService {

    private final GisPointDtoRepository gisPointDtoRepository;

    @Override
    public Optional<GisPointDTO> retrieveGisPointDtoById(Long id) {
        return gisPointDtoRepository.findById(id);
    }

}
