package com.senla.srs.service;

import com.senla.srs.model.ScooterType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ScooterTypeService {
    void save(ScooterType scooterType);
    List<ScooterType> retrieveAllScooterTypes();
    Optional<ScooterType> retrieveScooterTypeById(Long id);
    void delete(Long id);
}
