package com.senla.srs.service;

import com.senla.srs.dto.rentalsession.RentalSessionRequestDTO;

public interface RentalSessionValidator {
    boolean isValid(RentalSessionRequestDTO rentalSessionRequestDTO);
}
