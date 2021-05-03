package com.senla.srs.validator;

import com.senla.srs.dto.rentalsession.RentalSessionRequestDTO;
import com.senla.srs.entity.PromoCod;
import com.senla.srs.entity.Scooter;
import com.senla.srs.entity.SeasonTicket;
import com.senla.srs.entity.User;
import org.modelmapper.internal.Errors;

import java.util.Optional;

public interface RentalSessionRequestValidator {
    RentalSessionRequestDTO validate(RentalSessionRequestDTO rentalSessionRequestDTO,
                                     Optional<User> optionalUser,
                                     Optional<Scooter> optionalScooter,
                                     Optional<SeasonTicket> optionalSeasonTicket,
                                     Optional<PromoCod> optionalPromoCod,
                                     Errors errors);
}
