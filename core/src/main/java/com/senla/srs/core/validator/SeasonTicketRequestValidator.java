package com.senla.srs.core.validator;

import com.senla.srs.core.dto.seasonticket.SeasonTicketRequestDTO;
import com.senla.srs.core.entity.ScooterType;
import com.senla.srs.core.entity.SeasonTicket;
import com.senla.srs.core.entity.User;
import org.springframework.validation.Errors;

import java.util.Optional;

public interface SeasonTicketRequestValidator {

    SeasonTicketRequestDTO validate(SeasonTicketRequestDTO seasonTicketRequestDTO,
                                    Optional<SeasonTicket> optionalSeasonTicket,
                                    Optional<User> optionalUser,
                                    Optional<ScooterType> optionalScooterType,
                                    Errors errors);

}
