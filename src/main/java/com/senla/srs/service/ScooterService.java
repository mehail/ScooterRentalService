package com.senla.srs.service;

import com.senla.srs.model.Scooter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ScooterService {
    void save(Scooter scooter);
    List<Scooter> retrieveAllScooters();
    Optional<Scooter> retrieveScooterById(String id);
    void deleteById(Scooter scooter);
}
