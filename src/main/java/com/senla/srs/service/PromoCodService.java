package com.senla.srs.service;

import com.senla.srs.model.PromoCod;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface PromoCodService {
    PromoCod save(PromoCod promoCod);
    List<PromoCod> retrieveAllPromoCods();
    Optional<PromoCod> retrievePromoCodByName(String name);
    void deleteById(String name);
}
