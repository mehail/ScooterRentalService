package com.senla.srs.core.service.impl;

import com.senla.srs.core.dto.geo.CityDTO;
import com.senla.srs.core.repository.CityDtoRepository;
import com.senla.srs.core.service.CityDtoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class CityDtoServiceImpl implements CityDtoService {

    private final CityDtoRepository cityDtoRepository;

    @Override
    public Optional<CityDTO> retrieveGisPointDtoById(Long id) {
        return cityDtoRepository.findById(id);
    }

}
