package com.senla.srs.controller;

import com.senla.srs.model.PointOfRental;
import com.senla.srs.repository.PointOfRentalRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/point_of_rentals")
public class PointOfRentalControllerV1 {
    private PointOfRentalRepository pointOfRentalRepository;

    public PointOfRentalControllerV1(PointOfRentalRepository pointOfRentalRepository) {
        this.pointOfRentalRepository = pointOfRentalRepository;
    }

    @GetMapping
    public List<PointOfRental> getAll() {
        return pointOfRentalRepository.findAll();
    }
}
