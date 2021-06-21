package com.senla.srs.core.service.impl;

import com.senla.srs.core.entity.Scooter;
import com.senla.srs.core.repository.ScooterRepository;
import com.senla.srs.core.service.ScooterService;
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
public class ScooterServiceImpl implements ScooterService {

    private final ScooterRepository scooterRepository;

    @Override
    public Scooter save(Scooter scooter) {
        return scooterRepository.save(scooter);
    }

    @Override
    public Page<Scooter> retrieveAllScooters(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        return scooterRepository.findAll(paging);
    }

    @Override
    public Optional<Scooter> retrieveScooterBySerialNumber(String serialNumber) {
        return scooterRepository.findById(serialNumber);
    }

    @Override
    public void deleteById(String serialNumber) {
        scooterRepository.deleteById(serialNumber);
    }

}
