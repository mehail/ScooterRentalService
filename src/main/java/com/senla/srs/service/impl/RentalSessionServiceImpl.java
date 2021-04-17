package com.senla.srs.service.impl;

import com.senla.srs.model.RentalSession;
import com.senla.srs.repository.RentalSessionRepository;
import com.senla.srs.service.RentalSessionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RentalSessionServiceImpl implements RentalSessionService {
    private final RentalSessionRepository rentalSessionRepository;

    @Override
    public RentalSession save(RentalSession rentalSession) {
        return rentalSessionRepository.save(rentalSession);
    }

    @Override
    public List<RentalSession> retrieveAllRentalSessions() {
        return rentalSessionRepository.findAll();
    }

    @Override
    public List<RentalSession> retrieveAllRentalSessionsByUserId(Long userId) {
        return rentalSessionRepository.findAllByUserId(userId);
    }

    @Override
    public Optional<RentalSession> retrieveRentalSessionById(Long id) {
        return rentalSessionRepository.findById(id);
    }

    @Override
    public Optional<RentalSession> retrieveRentalSessionByUserIdAndScooterSerialNumberAndBegin(Long userId,
                                                                                               String scooterSerialNumber,
                                                                                               LocalDateTime begin) {

        return  rentalSessionRepository.findByUserIdAndScooterSerialNumberAndBegin(userId, scooterSerialNumber, begin);
    }

    @Override
    public void deleteById(Long id) {
        rentalSessionRepository.deleteById(id);
    }
}
