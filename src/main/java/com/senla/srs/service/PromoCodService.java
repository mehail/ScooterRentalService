package com.senla.srs.service;

import com.senla.srs.model.PromoCod;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface PromoCodService {
    PromoCod save(PromoCod promoCod);
    Page<PromoCod> retrieveAllPromoCods(Integer pageNo, Integer pageSize, String sortBy);
    Optional<PromoCod> retrievePromoCodByName(String name);
    void deleteById(String name);
}
