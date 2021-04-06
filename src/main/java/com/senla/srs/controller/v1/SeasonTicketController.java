package com.senla.srs.controller.v1;

import com.senla.srs.dto.SeasonTicketDTO;
import com.senla.srs.mapper.SeasonTicketMapper;
import com.senla.srs.model.User;
import com.senla.srs.service.SeasonTicketService;
import com.senla.srs.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Data
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/season_tickets")
public class SeasonTicketController {
    private SeasonTicketService seasonTicketService;
    private SeasonTicketMapper seasonTicketMapper;
    private UserService userService;
    private static final String NO_SEASON_TICKET_WITH_ID = "No season ticket with this ID found";

    @GetMapping
    @PreAuthorize("hasAuthority('seasonTickets:read')")
    public List<SeasonTicketDTO> getAll(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userSecurity) {

        if (userService.isAdmin(userSecurity)) {
            return seasonTicketService.retrieveAllSeasonTickets().stream()
                    .map(seasonTicket -> seasonTicketMapper.toDto(seasonTicket))
                    .collect(Collectors.toList());
        } else {
            try {
                User authUser = userService.retrieveUserByAuthenticationPrincipal(userSecurity).get();
                return seasonTicketService.retrieveAllSeasonTicketsByUserId(authUser.getId()).stream()
                        .map(seasonTicket -> seasonTicketMapper.toDto(seasonTicket))
                        .collect(Collectors.toList());
            } catch (NoSuchElementException e) {
                log.error(e.getMessage(), NO_SEASON_TICKET_WITH_ID);
                return new ArrayList<>();
            }
        }

    }
}
