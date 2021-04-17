package com.senla.srs.service;

import com.senla.srs.model.RentalSession;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public interface RentalSessionService {
    RentalSession save(RentalSession rentalSession);
    List<RentalSession> retrieveAllRentalSessions();
    List<RentalSession> retrieveAllRentalSessionsByUserId(Long id);
    Optional<RentalSession> retrieveRentalSessionById(Long id);
    Optional<RentalSession> retrieveRentalSessionByUserIdAndScooterSerialNumberAndBegin(Long userId,
                                                                                        String scooterSerialNumber,
                                                                                        LocalDateTime begin);

    void deleteById(Long id);
}
