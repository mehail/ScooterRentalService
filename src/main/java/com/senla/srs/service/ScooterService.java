package com.senla.srs.service;

import com.senla.srs.model.Scooter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ScooterService {
    Scooter save(Scooter scooter);
    Page<Scooter> retrieveAllScooters(Integer pageNo, Integer pageSize, String sortBy);
    Optional<Scooter> retrieveScooterBySerialNumber(String serialNumber);
    void deleteById(String serialNumber);
}
