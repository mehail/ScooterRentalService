package com.senla.srs.service;

import com.senla.srs.model.RentalSession;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public interface RentalSessionService {
    RentalSession save(RentalSession rentalSession);
    Page<RentalSession> retrieveAllRentalSessions(Integer pageNo, Integer pageSize, String sortBy);
    Page<RentalSession> retrieveAllRentalSessionsByUserId(Long id, Integer pageNo, Integer pageSize, String sortBy);
    Optional<RentalSession> retrieveRentalSessionById(Long id);
    Optional<RentalSession> retrieveRentalSessionByUserIdAndScooterSerialNumberAndBegin(Long userId,
                                                                                        String scooterSerialNumber,
                                                                                        LocalDateTime begin);

    void deleteById(Long id);
}
