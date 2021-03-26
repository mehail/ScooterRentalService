package com.senla.srs.service.impl;

import com.senla.srs.model.PointOfRental;
import com.senla.srs.repository.PointOfRentalRepository;
import com.senla.srs.service.PointOfRentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PointOfRentalServiceImpl implements PointOfRentalService {
    private final PointOfRentalRepository pointOfRentalRepository;

    @Autowired
    public PointOfRentalServiceImpl(PointOfRentalRepository pointOfRentalRepository) {
        this.pointOfRentalRepository = pointOfRentalRepository;
    }

    @Override
    public void save(PointOfRental pointOfRental) {
        pointOfRentalRepository.save(pointOfRental);
    }

    @Override
    public List<PointOfRental> retrieveAllPointOfRentals() {
        return pointOfRentalRepository.findAll();
    }

    @Override
    public Optional<PointOfRental> retrievePointOfRentalById(Long id) {
        return pointOfRentalRepository.findById(id);
    }

    @Override
    public void delete(PointOfRental pointOfRental) {
        pointOfRentalRepository.delete(pointOfRental);
    }
}
