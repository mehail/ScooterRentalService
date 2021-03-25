package com.senla.srs.service;

import com.senla.srs.model.SeasonTicket;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface SeasonTicketService {
    void save(SeasonTicket seasonTicket);
    List<SeasonTicket> retrieveAllSeasonTicketServices();
    Optional<SeasonTicket> retrieveSeasonTicketServiceById(Long id);
    void delete(SeasonTicketService seasonTicketService);
}
