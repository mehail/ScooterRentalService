package com.senla.srs.service.impl;

import com.senla.srs.model.SeasonTicket;
import com.senla.srs.repository.SeasonTicketRepository;
import com.senla.srs.service.SeasonTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeasonTicketServiceImpl implements SeasonTicketService {
    private final SeasonTicketRepository seasonTicketRepository;

    @Autowired
    public SeasonTicketServiceImpl(SeasonTicketRepository seasonTicketRepository) {
        this.seasonTicketRepository = seasonTicketRepository;
    }

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
    public void delete(SeasonTicket seasonTicket) {
        seasonTicketRepository.delete(seasonTicket);
    }

}
