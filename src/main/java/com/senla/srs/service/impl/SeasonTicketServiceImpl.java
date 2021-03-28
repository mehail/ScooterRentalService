package com.senla.srs.service.impl;

import com.senla.srs.model.SeasonTicket;
import com.senla.srs.repository.SeasonTicketRepository;
import com.senla.srs.service.SeasonTicketService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SeasonTicketServiceImpl implements SeasonTicketService {
    private final SeasonTicketRepository seasonTicketRepository;

    @Override
    public void save(SeasonTicket seasonTicket) {
        seasonTicketRepository.save(seasonTicket);
    }

    @Override
    public List<SeasonTicket> retrieveAllSeasonTickets() {
        return seasonTicketRepository.findAll();
    }

    @Override
    public Optional<SeasonTicket> retrieveSeasonTicketsById(Long id) {
        return seasonTicketRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        seasonTicketRepository.deleteById(id);
    }
}
