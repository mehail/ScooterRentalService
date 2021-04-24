package com.senla.srs.repository;

import com.senla.srs.model.RentalSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RentalSessionRepository extends JpaRepository<RentalSession, Long> {
    Page<RentalSession> findAllByUserId(Long userId, Pageable paging);
    Optional<RentalSession> findByUserIdAndScooterSerialNumberAndBegin(Long userId,
                                                                       String scooterSerialNumber,
                                                                       LocalDateTime begin);
}
