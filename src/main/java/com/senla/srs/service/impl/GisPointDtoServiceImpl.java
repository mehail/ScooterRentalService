package com.senla.srs.service.impl;

import com.senla.srs.dto.geo.GisPointDTO;
import com.senla.srs.repository.GisPointDtoRepository;
import com.senla.srs.service.GisPointDtoService;
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
