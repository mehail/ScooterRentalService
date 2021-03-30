package com.senla.srs.service.impl;

import com.senla.srs.model.PromoCod;
import com.senla.srs.repository.PromoCodRepository;
import com.senla.srs.service.PromoCodService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PromoCodServiceImpl implements PromoCodService {
    private final PromoCodRepository promoCodRepository;

    @Override
    public void save(PromoCod promoCod) {
        promoCodRepository.save(promoCod);
    }

    @Override
    public List<PromoCod> retrieveAllPromoCods() {
        return promoCodRepository.findAll();
    }

    @Override
    public Optional<PromoCod> retrievePromoCodByName(String name) {
        return promoCodRepository.findById(name);
    }

    @Override
    public void deleteById(String name) {
        promoCodRepository.deleteById(name);
    }
}
