package com.senla.srs.core.service;

import com.senla.srs.core.entity.SeasonTicket;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.Optional;

public interface SeasonTicketService {

    SeasonTicket save(SeasonTicket seasonTicket);

    Page<SeasonTicket> retrieveAllSeasonTickets(Integer pageNo, Integer pageSize, String sortBy);

    Page<SeasonTicket> retrieveAllSeasonTicketsByUserId(Long userId, Integer pageNo, Integer pageSize, String sortBy);

    Optional<SeasonTicket> retrieveSeasonTicketsById(Long id);

    Optional<SeasonTicket> retrieveSeasonTicketByUserIdAndScooterTypeIdAndStartDate(Long userId,
                                                                                    Long scooterTypeId,
                                                                                    LocalDate startDate);

    void deleteById(Long id);

}
