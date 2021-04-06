package com.senla.srs.controller.v1;

import com.senla.srs.dto.ScooterTypeDTO;
import com.senla.srs.dto.SeasonTicketDTO;
import com.senla.srs.mapper.SeasonTicketMapper;
import com.senla.srs.service.SeasonTicketService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Data
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/season_tickets")
public class SeasonTicketController {
    private SeasonTicketService seasonTicketService;
    private SeasonTicketMapper seasonTicketMapper;

    @GetMapping
    @PreAuthorize("hasAuthority('seasonTickets:readAll')")
    public List<SeasonTicketDTO> getAll() {
        return seasonTicketService.retrieveAllSeasonTickets().stream()
                .map(seasonTicket -> seasonTicketMapper.toDto(seasonTicket))
                .collect(Collectors.toList());
    }






}
