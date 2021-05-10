package com.senla.srs.service;

import com.senla.srs.entity.ScooterType;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ScooterTypeService {

    ScooterType save(ScooterType scooterType);

    Page<ScooterType> retrieveAllScooterTypes(Integer pageNo, Integer pageSize, String sortBy);

    Optional<ScooterType> retrieveScooterTypeById(Long id);

    Optional<ScooterType> retrieveScooterTypeByModel(String model);

    void deleteById(Long id);

}
