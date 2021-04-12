package com.senla.srs.service.impl;

import com.senla.srs.model.RentalSession;
import com.senla.srs.model.Scooter;
import com.senla.srs.model.User;
import com.senla.srs.repository.RentalSessionRepository;
import com.senla.srs.service.RentalSessionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RentalSessionServiceImpl implements RentalSessionService {
    private final RentalSessionRepository rentalSessionRepository;

    @Override
    public void save(RentalSession rentalSession) {
        rentalSessionRepository.save(rentalSession);
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
    public Optional<RentalSession> retrieveRentalSessionByUserAndScooterAndBegin(User user, Scooter scooter, LocalDate begin) {
        return rentalSessionRepository.findRentalSessionByUserAndScooterAndBegin(user, scooter, begin);
    }

    @Override
    public Optional<RentalSession> retrieveRentalSessionByUserIdAndScooterSerialNumberAndBegin(Long userId,
                                                                                               String scooterSerialNumber,
                                                                                               LocalDate begin) {
        return rentalSessionRepository.findRentalSessionByUser_IdAndScooter_SerialNumberAndBegin(userId,
                scooterSerialNumber, begin);
    }

    @Override
    public void deleteById(Long id) {
        rentalSessionRepository.deleteById(id);
    }
}
