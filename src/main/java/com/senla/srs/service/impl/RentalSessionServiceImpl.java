package com.senla.srs.service.impl;

import com.senla.srs.model.RentalSession;
import com.senla.srs.repository.RentalSessionRepository;
import com.senla.srs.service.RentalSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RentalSessionServiceImpl implements RentalSessionService {

    private final RentalSessionRepository rentalSessionRepository;

    @Autowired
    public RentalSessionServiceImpl(RentalSessionRepository rentalSessionRepository) {
        this.rentalSessionRepository = rentalSessionRepository;
    }

    @Override
    public void save(RentalSession rentalSession) {
        rentalSessionRepository.save(rentalSession);
    }

    @Override
    public List<RentalSession> retrieveAllRentalSessions() {
        return rentalSessionRepository.findAll();
    }

    @Override
    public Optional<RentalSession> retrieveRentalSessionById(Long id) {
        return rentalSessionRepository.findById(id);
    }

    @Override
    public void delete(RentalSession rentalSession) {
        rentalSessionRepository.delete(rentalSession);
    }

}
