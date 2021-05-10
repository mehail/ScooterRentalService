package com.senla.srs.service.impl;

import com.senla.srs.entity.PointOfRental;
import com.senla.srs.repository.PointOfRentalRepository;
import com.senla.srs.service.PointOfRentalService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class PointOfRentalServiceImpl implements PointOfRentalService {

    private final PointOfRentalRepository pointOfRentalRepository;

    @Override
    public PointOfRental save(PointOfRental pointOfRental) {
        return pointOfRentalRepository.save(pointOfRental);
    }

    @Override
    public Page<PointOfRental> retrieveAllPointOfRentals(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        return pointOfRentalRepository.findAll(paging);
    }

    @Override
    public Optional<PointOfRental> retrievePointOfRentalById(Long id) {
        return pointOfRentalRepository.findById(id);
    }

    @Override
    public Optional<PointOfRental> retrievePointOfRentalByName(String name) {
        return pointOfRentalRepository.findByName(name);
    }

    @Override
    public void deleteById(Long id) {
        pointOfRentalRepository.deleteById(id);
    }

}
