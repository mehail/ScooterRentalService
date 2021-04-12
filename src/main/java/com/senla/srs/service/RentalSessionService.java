package com.senla.srs.service;

import com.senla.srs.model.RentalSession;
import com.senla.srs.model.Scooter;
import com.senla.srs.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public interface RentalSessionService {
    void save(RentalSession rentalSession);
    List<RentalSession> retrieveAllRentalSessions();
    List<RentalSession> retrieveAllRentalSessionsByUserId(Long id);
    Optional<RentalSession> retrieveRentalSessionById(Long id);
    Optional<RentalSession> retrieveRentalSessionByUserAndScooterAndBegin(User user, Scooter scooter, LocalDate begin);
    Optional<RentalSession> retrieveRentalSessionByUserIdAndScooterSerialNumberAndBegin(Long userId,
                                                                                        String scooterSerialNumber,
                                                                                        LocalDate begin);
    void deleteById(Long id);
}
