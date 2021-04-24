package com.senla.srs.service;

import com.senla.srs.model.PointOfRental;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface PointOfRentalService {
    PointOfRental save(PointOfRental pointOfRental);
    Page<PointOfRental> retrieveAllPointOfRentals(Integer pageNo, Integer pageSize, String sortBy);
    Optional<PointOfRental> retrievePointOfRentalById(Long id);
    Optional<PointOfRental> retrievePointOfRentalByName(String name);
    void deleteById(Long id);
}
