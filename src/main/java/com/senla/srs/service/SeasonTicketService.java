package com.senla.srs.service;

import com.senla.srs.model.SeasonTicket;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public interface SeasonTicketService {
    SeasonTicket save(SeasonTicket seasonTicket);
    Page<SeasonTicket> retrieveAllSeasonTickets(Integer pageNo, Integer pageSize, String sortBy);
    Page<SeasonTicket> retrieveAllSeasonTicketsByUserId(Long userId, Integer pageNo, Integer pageSize, String sortBy);
    Optional<SeasonTicket> retrieveSeasonTicketsById(Long id);
    Optional<SeasonTicket> retrieveSeasonTicketByUserIdAndScooterTypeIdAndStartDate(Long userId, Long scooterTypeId,
                                                                                  LocalDate startDate);
    void deleteById(Long id);
}
