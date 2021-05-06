package com.senla.srs.service.impl;

import com.senla.srs.entity.PromoCod;
import com.senla.srs.repository.PromoCodRepository;
import com.senla.srs.service.PromoCodService;
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
public class PromoCodServiceImpl implements PromoCodService {
    private final PromoCodRepository promoCodRepository;

    @Override
    public PromoCod save(PromoCod promoCod) {
        return promoCodRepository.save(promoCod);
    }

    @Override
    public Page<PromoCod> retrieveAllPromoCods(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        return promoCodRepository.findAll(paging);
    }

    @Override
    public Optional<PromoCod> retrievePromoCodByName(String name) {
        return promoCodRepository.findByName(name);
    }

    @Override
    public void deleteById(String name) {
        promoCodRepository.deleteById(name);
    }
}
