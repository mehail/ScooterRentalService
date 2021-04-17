package com.senla.srs.service;

import com.senla.srs.model.PointOfRental;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface PointOfRentalService {
    PointOfRental save(PointOfRental pointOfRental);
    List<PointOfRental> retrieveAllPointOfRentals();
    Optional<PointOfRental> retrievePointOfRentalById(Long id);
    Optional<PointOfRental> retrievePointOfRentalByName(String name);
    void deleteById(Long id);
}
