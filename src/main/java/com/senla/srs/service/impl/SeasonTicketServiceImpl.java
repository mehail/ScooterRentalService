package com.senla.srs.service.impl;

import com.senla.srs.model.ScooterType;
import com.senla.srs.model.SeasonTicket;
import com.senla.srs.repository.SeasonTicketRepository;
import com.senla.srs.service.SeasonTicketService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    public List<SeasonTicket> retrieveAllSeasonTicketsByUserId(Long userId) {
        return seasonTicketRepository.findAllByUserId(userId);
    }

    @Override
    public Optional<SeasonTicket> retrieveSeasonTicketsById(Long id) {
        return seasonTicketRepository.findById(id);
    }

    @Override
    public Optional<SeasonTicket> retrieveSeasonTicketByUserIdAndScooterTypeAndStartDate(Long userId, ScooterType scooterType, LocalDate startDate) {
        return seasonTicketRepository.findSeasonTicketByUserIdAndScooterTypeAndStartDate(userId, scooterType, startDate);
    }

    @Override
    public void deleteById(Long id) {
        seasonTicketRepository.deleteById(id);
    }
}
