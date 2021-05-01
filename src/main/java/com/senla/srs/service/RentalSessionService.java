package com.senla.srs.service;

import com.senla.srs.entity.RentalSession;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
public interface RentalSessionService {
    RentalSession save(RentalSession rentalSession);
    Page<RentalSession> retrieveAllRentalSessions(Integer pageNo, Integer pageSize, String sortBy);
    Page<RentalSession> retrieveAllRentalSessionsByUserId(Long id, Integer pageNo, Integer pageSize, String sortBy);
    Optional<RentalSession> retrieveRentalSessionById(Long id);
    Optional<RentalSession> retrieveRentalSessionByUserIdAndScooterSerialNumberAndBeginDateAndBeginTime(Long userId,
                                                                                                        String scooterSerialNumber,
                                                                                                        LocalDate beginDate, LocalTime beginTime);

    void deleteById(Long id);
}
