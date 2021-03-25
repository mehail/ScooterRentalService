package com.senla.srs.service;

import com.senla.srs.model.RentalSession;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface RentalSessionService {
    void save(RentalSession rentalSession);
    List<RentalSession> retrieveAllRentalSessions();
    Optional<RentalSession> retrieveRentalSessionById(Long id);
    void delete(RentalSession rentalSession);
}
