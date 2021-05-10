package com.senla.srs.validator;

import com.senla.srs.dto.seasonticket.SeasonTicketRequestDTO;
import com.senla.srs.entity.ScooterType;
import com.senla.srs.entity.SeasonTicket;
import com.senla.srs.entity.User;
import org.springframework.validation.Errors;

import java.util.Optional;

public interface SeasonTicketRequestValidator {

    SeasonTicketRequestDTO validate(SeasonTicketRequestDTO seasonTicketRequestDTO,
                                    Optional<SeasonTicket> optionalSeasonTicket,
                                    Optional<User> optionalUser,
                                    Optional<ScooterType> optionalScooterType,
                                    Errors errors);

}
