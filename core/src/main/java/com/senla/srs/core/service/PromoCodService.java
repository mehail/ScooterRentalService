package com.senla.srs.core.service;

import com.senla.srs.core.entity.PromoCod;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface PromoCodService {

    PromoCod save(PromoCod promoCod);

    Page<PromoCod> retrieveAllPromoCods(Integer pageNo, Integer pageSize, String sortBy);

    Optional<PromoCod> retrievePromoCodByName(String name);

    void deleteByName(String name);

}
