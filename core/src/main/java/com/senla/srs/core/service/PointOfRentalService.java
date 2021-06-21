package com.senla.srs.core.service;

import com.senla.srs.core.entity.PointOfRental;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface PointOfRentalService {

    PointOfRental save(PointOfRental pointOfRental);

    Page<PointOfRental> retrieveAllPointOfRentals(Integer pageNo, Integer pageSize, String sortBy);

    Optional<PointOfRental> retrievePointOfRentalById(Long id);

    Optional<PointOfRental> retrievePointOfRentalByName(String name);

    void deleteById(Long id);

}
