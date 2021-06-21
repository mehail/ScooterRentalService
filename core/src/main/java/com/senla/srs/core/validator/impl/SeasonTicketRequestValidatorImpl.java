package com.senla.srs.core.validator.impl;

import com.senla.srs.core.dto.seasonticket.SeasonTicketRequestDTO;
import com.senla.srs.core.entity.ScooterType;
import com.senla.srs.core.entity.SeasonTicket;
import com.senla.srs.core.entity.User;
import com.senla.srs.core.validator.SeasonTicketRequestValidator;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.Optional;

@Service
public class SeasonTicketRequestValidatorImpl implements SeasonTicketRequestValidator {

    private static final String PRICE = "price";

    @Override
    public SeasonTicketRequestDTO validate(SeasonTicketRequestDTO seasonTicketRequestDTO,
                                           Optional<SeasonTicket> optionalSeasonTicket,
                                           Optional<User> optionalUser,
                                           Optional<ScooterType> optionalScooterType,
                                           Errors errors) {

        if (optionalSeasonTicket.isPresent()) {
            errors.reject("exist season ticket", "The existing season ticket is closed for modification");
        } else {

            if (optionalUser.isEmpty()) {
                errors.reject("userId", "User with this ID does not exist");
            } else if (seasonTicketRequestDTO.getPrice() >= optionalUser.get().getBalance()) {
                errors.reject(PRICE, "User balance must be greater than or equal to price");
            }

            if (optionalScooterType.isEmpty()) {
                errors.reject("scooterTypeId", "Scooter type with this ID does not exist");
            } else if (seasonTicketRequestDTO.getPrice() < optionalScooterType.get().getPricePerMinute()) {
                errors.reject(PRICE, "The price should be greater than the price per minute of this scooter type");
            }

        }

        return seasonTicketRequestDTO;
    }

}
