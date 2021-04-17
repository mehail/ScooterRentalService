package com.senla.srs.service;

import com.senla.srs.dto.rentalsession.RentalSessionRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface RentalSessionValidator {
    boolean isValid(RentalSessionRequestDTO rentalSessionRequestDTO);
}
