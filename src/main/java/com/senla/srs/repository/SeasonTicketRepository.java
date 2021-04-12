package com.senla.srs.repository;

import com.senla.srs.model.SeasonTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SeasonTicketRepository extends JpaRepository<SeasonTicket, Long> {
    List<SeasonTicket> findAllByUserId(Long userId);
    Optional<SeasonTicket> findSeasonTicketByUserIdAndScooterTypeIdAndStartDate(Long userId, Long scooterTypeId,
                                                                                LocalDate startDate);
}