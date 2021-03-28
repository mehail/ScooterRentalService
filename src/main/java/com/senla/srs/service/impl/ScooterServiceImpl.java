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
    public void save(Scooter scooter) {
        scooterRepository.save(scooter);
    }

    @Override
    public List<Scooter> retrieveAllScooters() {
        return scooterRepository.findAll();
    }

    @Override
    public Optional<Scooter> retrieveScooterById(String id) {
        return scooterRepository.findById(id);
    }

    @Override
    public void deleteById(Scooter scooter) {
        scooterRepository.delete(scooter);
    }
}
