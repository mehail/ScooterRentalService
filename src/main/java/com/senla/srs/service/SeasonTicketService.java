package com.senla.srs.service;

import com.senla.srs.model.ScooterType;
import com.senla.srs.model.SeasonTicket;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public interface SeasonTicketService {
    void save(SeasonTicket seasonTicket);
    List<SeasonTicket> retrieveAllSeasonTickets();
    List<SeasonTicket> retrieveAllSeasonTicketsByUserId(Long userId);
    Optional<SeasonTicket> retrieveSeasonTicketsById(Long id);
    Optional<SeasonTicket> retrieveSeasonTicketByUserIdAndScooterTypeAndStartDate(Long userId, ScooterType scooterType,
                                                                                  LocalDate startDate);
    void deleteById(Long id);
}
