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
    Optional<RentalSession> findRentalSessionByUserAndScooterAndBegin(User user, Scooter scooter, LocalDate begin);
    Optional<RentalSession> findRentalSessionByUser_IdAndScooter_SerialNumberAndBegin(Long userId,
                                                                                      String scooterSerialNumber,
                                                                                      LocalDate begin);
}
