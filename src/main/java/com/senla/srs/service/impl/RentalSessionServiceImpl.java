package com.senla.srs.service.impl;

import com.senla.srs.entity.RentalSession;
import com.senla.srs.repository.RentalSessionRepository;
import com.senla.srs.service.RentalSessionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
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
    public Page<RentalSession> retrieveAllRentalSessions(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        return rentalSessionRepository.findAll(paging);
    }

    @Override
    public Page<RentalSession> retrieveAllRentalSessionsByUserId(Long userId, Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        return rentalSessionRepository.findAllByUserId(userId, paging);
    }

    @Override
    public Optional<RentalSession> retrieveRentalSessionById(Long id) {
        return rentalSessionRepository.findById(id);
    }

    @Override
    public Optional<RentalSession> retrieveRentalSessionByUserIdAndScooterSerialNumberAndBeginDateAndBeginTime(Long userId,
                                                                                                               String scooterSerialNumber,
                                                                                                               LocalDate beginDate, LocalTime beginTime) {

        return  rentalSessionRepository.findByUserIdAndScooterSerialNumberAndBeginDateAndBeginTime(userId,
                scooterSerialNumber, beginDate, beginTime);
    }

    @Override
    public void deleteById(Long id) {
        rentalSessionRepository.deleteById(id);
    }
}
