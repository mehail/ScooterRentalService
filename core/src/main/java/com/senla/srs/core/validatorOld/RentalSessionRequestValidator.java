package com.senla.srs.core.validatorOld;

import com.senla.srs.core.dto.rentalsession.RentalSessionRequestDTO;
import com.senla.srs.core.entity.*;
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
