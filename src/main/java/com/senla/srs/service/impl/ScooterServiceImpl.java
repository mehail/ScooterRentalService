package com.senla.srs.service.impl;

import com.senla.srs.model.Scooter;
import com.senla.srs.repository.ScooterRepository;
import com.senla.srs.service.ScooterService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ScooterServiceImpl implements ScooterService {
    private final ScooterRepository scooterRepository;

    @Override
    public Scooter save(Scooter scooter) {
        return scooterRepository.save(scooter);
    }

    @Override
    public List<Scooter> retrieveAllScooters() {
        return scooterRepository.findAll();
    }

    @Override
    public Optional<Scooter> retrieveScooterBySerialNumber(String serialNumber) {
        return scooterRepository.findById(serialNumber);
    }

    @Override
    public void deleteById(String serialNumber) {
        scooterRepository.deleteById(serialNumber);
    }
}
