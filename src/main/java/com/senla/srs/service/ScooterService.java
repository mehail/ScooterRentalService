package com.senla.srs.service;

import com.senla.srs.model.Scooter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ScooterService {
    Scooter save(Scooter scooter);
    List<Scooter> retrieveAllScooters();
    Optional<Scooter> retrieveScooterBySerialNumber(String serialNumber);
    void deleteById(String serialNumber);
}
