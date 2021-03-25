package com.senla.srs.service.impl;

import com.senla.srs.model.Scooter;
import com.senla.srs.repository.ScooterRepository;
import com.senla.srs.service.ScooterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScooterServiceImpl implements ScooterService {

    private final ScooterRepository scooterRepository;

    @Autowired
    public ScooterServiceImpl(ScooterRepository scooterRepository) {
        this.scooterRepository = scooterRepository;
    }

    @Override
    public void save(Scooter scooter) {
        scooterRepository.save(scooter);
    }

    @Override
    public List<Scooter> retrieveAllScooters() {
        return scooterRepository.findAll();
    }

    @Override
    public Optional<Scooter> retrieveScooterById(Long id) {
        return scooterRepository.findById(id);
    }

    @Override
    public void delete(Scooter scooter) {
        scooterRepository.delete(scooter);
    }

}
