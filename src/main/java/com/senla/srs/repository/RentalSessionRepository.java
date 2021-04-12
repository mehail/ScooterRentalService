package com.senla.srs.repository;

import com.senla.srs.model.RentalSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RentalSessionRepository extends JpaRepository<RentalSession, Long> {
    List<RentalSession> findAllByUserId(Long userId);
    Optional<RentalSession> findByUserIdAndScooterSerialNumberAndBegin(Long userId,
                                                                       String scooterSerialNumber,
                                                                       LocalDateTime begin);
    Optional<RentalSession> findByUserIdAndScooterSerialNumber(Long userId,
                                                               String scooterSerialNumber);
}
