package com.senla.srs.service.impl;

import com.senla.srs.entity.SeasonTicket;
import com.senla.srs.repository.SeasonTicketRepository;
import com.senla.srs.service.SeasonTicketService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    public Page<SeasonTicket> retrieveAllSeasonTickets(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        return seasonTicketRepository.findAll(paging);
    }

    @Override
    public Page<SeasonTicket> retrieveAllSeasonTicketsByUserId(Long userId, Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        return seasonTicketRepository.findAllByUserId(userId, paging);
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
