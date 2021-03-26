package com.senla.srs.service.impl;

import com.senla.srs.model.ScooterType;
import com.senla.srs.repository.ScooterTypeRepository;
import com.senla.srs.service.ScooterTypeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ScooterTypeServiceImpl implements ScooterTypeService {
    private final ScooterTypeRepository scooterTypeRepository;

    @Override
    public void save(ScooterType scooterType) {
        scooterTypeRepository.save(scooterType);
    }

    @Override
    public List<ScooterType> retrieveAllScooterTypes() {
        return scooterTypeRepository.findAll();
    }

    @Override
    public Optional<ScooterType> retrieveScooterTypeById(Long id) {
        return scooterTypeRepository.findById(id);
    }

    @Override
    public void delete(ScooterType scooterType) {
        scooterTypeRepository.delete(scooterType);
    }
}
