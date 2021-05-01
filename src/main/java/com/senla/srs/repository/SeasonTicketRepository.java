package com.senla.srs.repository;

import com.senla.srs.entity.SeasonTicket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface SeasonTicketRepository extends JpaRepository<SeasonTicket, Long> {
    Page<SeasonTicket> findAllByUserId(Long userId, Pageable paging);
    Optional<SeasonTicket> findSeasonTicketByUserIdAndScooterTypeIdAndStartDate(Long userId, Long scooterTypeId,
                                                                                LocalDate startDate);
}