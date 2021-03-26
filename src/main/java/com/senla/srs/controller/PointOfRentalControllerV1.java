package com.senla.srs.controller;

import com.senla.srs.model.PointOfRental;
import com.senla.srs.repository.PointOfRentalRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Data
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/point_of_rentals")
public class PointOfRentalControllerV1 {
    private PointOfRentalRepository pointOfRentalRepository;

    @GetMapping
    public List<PointOfRental> getAll() {
        return pointOfRentalRepository.findAll();
    }
}
