package com.senla.srs.core.validator;

import com.senla.srs.core.dto.seasonticket.SeasonTicketRequestDTO;
import com.senla.srs.core.entity.ScooterType;
import com.senla.srs.core.entity.SeasonTicket;
import com.senla.srs.core.entity.User;
import com.senla.srs.core.service.ScooterTypeService;
import com.senla.srs.core.service.SeasonTicketService;
import com.senla.srs.core.service.UserService;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Service("seasonTicketRequestValidator")
public class SeasonTicketRequestValidator implements Validator {

    private static final String PRICE = "price";
    private final SeasonTicketService seasonTicketService;
    private final UserService userService;
    private final ScooterTypeService scooterTypeService;

    public SeasonTicketRequestValidator(SeasonTicketService seasonTicketService,
                                        UserService userService,
                                        ScooterTypeService scooterTypeService) {
        this.seasonTicketService = seasonTicketService;
        this.userService = userService;
        this.scooterTypeService = scooterTypeService;
    }

    @Override
    public boolean supports(@NonNull Class<?> aClass) {
        return SeasonTicketRequestDTO.class.equals(aClass);
    }

    @Override
    public void validate(@NonNull Object o, @NonNull Errors errors) {

        var seasonTicketRequestDTO = (SeasonTicketRequestDTO) o;
        Optional<SeasonTicket> optionalSeasonTicket =
                seasonTicketService.retrieveSeasonTicketByUserIdAndScooterTypeIdAndStartDate(seasonTicketRequestDTO.getUserId(),
                        seasonTicketRequestDTO.getScooterTypeId(),
                        seasonTicketRequestDTO.getStartDate());
        Optional<User> optionalUser = userService.retrieveUserById(seasonTicketRequestDTO.getUserId());
        Optional<ScooterType> optionalScooterType =
                scooterTypeService.retrieveScooterTypeById(seasonTicketRequestDTO.getScooterTypeId());

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

    }
}
