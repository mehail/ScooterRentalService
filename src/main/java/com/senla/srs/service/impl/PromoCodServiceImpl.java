package com.senla.srs.service.impl;

import com.senla.srs.model.PromoCod;
import com.senla.srs.repository.PromoCodRepository;
import com.senla.srs.service.PromoCodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PromoCodServiceImpl implements PromoCodService {
    private final PromoCodRepository promoCodRepository;

    @Autowired
    public PromoCodServiceImpl(PromoCodRepository promoCodRepository) {
        this.promoCodRepository = promoCodRepository;
    }

    @Override
    public void save(PromoCod promoCod) {
        promoCodRepository.save(promoCod);
    }

    @Override
    public List<PromoCod> retrieveAllPromoCods() {
        return promoCodRepository.findAll();
    }

    @Override
    public Optional<PromoCod> retrievePromoCodById(Long id) {
        return promoCodRepository.findById(id);
    }

    @Override
    public void delete(PromoCod promoCod) {
        promoCodRepository.delete(promoCod);
    }

}
