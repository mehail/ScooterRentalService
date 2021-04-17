package com.senla.srs.service.impl;

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
    public SeasonTicket save(SeasonTicket seasonTicket) {
        return seasonTicketRepository.save(seasonTicket);
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
    public Optional<SeasonTicket> retrieveSeasonTicketByUserIdAndScooterTypeIdAndStartDate(Long userId, Long scooterTypeId,
                                                                                           LocalDate startDate) {
        return seasonTicketRepository.findSeasonTicketByUserIdAndScooterTypeIdAndStartDate(userId, scooterTypeId, startDate);
    }

    @Override
    public void deleteById(Long id) {
        seasonTicketRepository.deleteById(id);
    }
}
