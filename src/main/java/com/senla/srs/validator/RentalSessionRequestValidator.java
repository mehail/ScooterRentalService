package com.senla.srs.validator;

import com.senla.srs.dto.rentalsession.RentalSessionRequestDTO;
import com.senla.srs.entity.*;
import org.springframework.validation.Errors;


import java.util.Optional;

public interface RentalSessionRequestValidator {

    RentalSessionRequestDTO validate(RentalSessionRequestDTO rentalSessionRequestDTO,
                                     Optional<RentalSession> optionalRentalSession,
                                     Optional<User> optionalUser,
                                     Optional<Scooter> optionalScooter,
                                     Optional<SeasonTicket> optionalSeasonTicket,
                                     Optional<PromoCod> optionalPromoCod,
                                     Errors errors);

}
