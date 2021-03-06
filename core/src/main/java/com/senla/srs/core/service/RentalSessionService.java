package com.senla.srs.core.service;

import com.senla.srs.core.entity.RentalSession;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface RentalSessionService {

    RentalSession save(RentalSession rentalSession);

    Page<RentalSession> retrieveAllRentalSessions(Integer pageNo, Integer pageSize, String sortBy);

    Page<RentalSession> retrieveAllRentalSessionsByUserId(Long id, Integer pageNo, Integer pageSize, String sortBy);

    Optional<RentalSession> retrieveRentalSessionById(Long id);

    Optional<RentalSession> retrieveRentalSessionByUserIdAndScooterSerialNumberAndBeginDateAndBeginTime(Long userId,
                                                                                                        String scooterSerialNumber,
                                                                                                        LocalDate beginDate,
                                                                                                        LocalTime beginTime);

    List<RentalSession> retrieveRentalSessionByScooterSerialNumber(String scooterSerialNumber);

    void deleteById(Long id);

}
