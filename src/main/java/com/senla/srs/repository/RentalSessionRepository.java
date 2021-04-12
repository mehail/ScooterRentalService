package com.senla.srs.repository;

import com.senla.srs.model.RentalSession;
import com.senla.srs.model.Scooter;
import com.senla.srs.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RentalSessionRepository extends JpaRepository<RentalSession, Long> {
    List<RentalSession> findAllByUserId(Long userId);
    Optional<RentalSession> findByUserAndScooterAndBegin(User user, Scooter scooter, LocalDate begin);
    Optional<RentalSession> findByUserIdAndScooterSerialNumber(Long userId,
                                                               String scooterSerialNumber);
    Optional<RentalSession> findByUserIdAndScooterSerialNumberAndBegin(Long userId,
                                                               String scooterSerialNumber, LocalDate begin);
}
