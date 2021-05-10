package com.senla.srs.service.impl;

import com.senla.srs.entity.ScooterType;
import com.senla.srs.repository.ScooterTypeRepository;
import com.senla.srs.service.ScooterTypeService;
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
public class ScooterTypeServiceImpl implements ScooterTypeService {

    private final ScooterTypeRepository scooterTypeRepository;

    @Override
    public ScooterType save(ScooterType scooterType) {
        return scooterTypeRepository.save(scooterType);
    }

    @Override
    public Page<ScooterType> retrieveAllScooterTypes(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        return scooterTypeRepository.findAll(paging);
    }

    @Override
    public Optional<ScooterType> retrieveScooterTypeById(Long id) {
        return scooterTypeRepository.findById(id);
    }

    @Override
    public Optional<ScooterType> retrieveScooterTypeByModel(String model) {
        return scooterTypeRepository.findByModel(model);
    }

    @Override
    public void deleteById(Long id) {
        scooterTypeRepository.deleteById(id);
    }

}
