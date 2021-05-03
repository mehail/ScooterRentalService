package com.senla.srs.validator.impl;

import com.senla.srs.dto.rentalsession.RentalSessionRequestDTO;
import com.senla.srs.entity.*;
import com.senla.srs.validator.RentalSessionRequestValidator;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.Optional;

@Service
public class RentalSessionRequestValidatorImpl implements RentalSessionRequestValidator {
    @Override
    public RentalSessionRequestDTO validate(RentalSessionRequestDTO rentalSessionRequestDTO,
                                            Optional<RentalSession> optionalRentalSession,
                                            Optional<User> optionalUser,
                                            Optional<Scooter> optionalScooter,
                                            Optional<SeasonTicket> optionalSeasonTicket,
                                            Optional<PromoCod> optionalPromoCod,
                                            Errors errors) {

        return optionalRentalSession.isPresent()
                ? validateNewEntity(rentalSessionRequestDTO,
                optionalRentalSession,
                optionalUser,
                optionalScooter,
                optionalSeasonTicket,
                optionalPromoCod,
                errors)

                : validateExistEntity(rentalSessionRequestDTO,
                optionalRentalSession,
                optionalUser,
                optionalScooter,
                optionalSeasonTicket,
                optionalPromoCod,
                errors);
    }

    //ToDo else соответствие всех полей полей rentalSessionDto.end != null

    private RentalSessionRequestDTO validateNewEntity(RentalSessionRequestDTO rentalSessionRequestDTO,
                                                      Optional<RentalSession> optionalRentalSession,
                                                      Optional<User> optionalUser,
                                                      Optional<Scooter> optionalScooter,
                                                      Optional<SeasonTicket> optionalSeasonTicket,
                                                      Optional<PromoCod> optionalPromoCod,
                                                      Errors errors) {
        if (optionalScooter.isEmpty()) {
            errors.reject("scooterSerialNumber", "Scooter with this serial number does not exist");
        } else if (optionalScooter.get().getStatus() != ScooterStatus.AVAILABLE) {
            errors.reject("scooterSerialNumber", "Scooter with this serial number not available for use");
        }


        if (rentalSessionRequestDTO.getSeasonTicketId() != null) {
            if (optionalSeasonTicket.isEmpty()) {
                errors.reject("seasonTicketId", "Season ticket with this ID does not exist");
            } else if (!optionalSeasonTicket.get().getAvailableForUse()) {
                errors.reject("seasonTicketId", "Season ticket with this ID not available for use");
            } else if (!rentalSessionRequestDTO.getBegin().toLocalDate().isBefore(optionalSeasonTicket.get().getStartDate())
            && rentalSessionRequestDTO.getBegin().toLocalDate().isBefore(optionalSeasonTicket.get().getExpiredDate())) {

            }
        }



        //ToDo else if проверка на попадание диапазона абонемента в начало сессии

        //ToDo scooter.scooterType == seasonTicket.scooterType

        if (rentalSessionRequestDTO.getPromoCodName() != null
                && optionalPromoCod.isEmpty()) {
            errors.reject("promoCodName", "PromoCod with this name does not exist");
        }
        //ToDo else if проверка на попадание диапазона абонемента в начало сессии, доступность

        return rentalSessionRequestDTO;
    }

    private RentalSessionRequestDTO validateExistEntity(RentalSessionRequestDTO rentalSessionRequestDTO,
                                                        Optional<RentalSession> optionalRentalSession,
                                                        Optional<User> optionalUser,
                                                        Optional<Scooter> optionalScooter,
                                                        Optional<SeasonTicket> optionalSeasonTicket,
                                                        Optional<PromoCod> optionalPromoCod,
                                                        Errors errors) {


        return rentalSessionRequestDTO;
    }

}
