package com.senla.srs.service;

import com.senla.srs.entity.Scooter;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ScooterService {

    Scooter save(Scooter scooter);

    Page<Scooter> retrieveAllScooters(Integer pageNo, Integer pageSize, String sortBy);

    Optional<Scooter> retrieveScooterBySerialNumber(String serialNumber);

    void deleteById(String serialNumber);

}
