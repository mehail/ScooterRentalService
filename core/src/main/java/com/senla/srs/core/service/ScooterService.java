package com.senla.srs.core.service;

import com.senla.srs.core.entity.Scooter;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ScooterService {

    Scooter save(Scooter scooter);

    Page<Scooter> retrieveAllScooters(Integer pageNo, Integer pageSize, String sortBy);

    Optional<Scooter> retrieveScooterBySerialNumber(String serialNumber);

    void deleteById(String serialNumber);

}
