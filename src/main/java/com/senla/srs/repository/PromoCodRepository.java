package com.senla.srs.repository;

import com.senla.srs.model.PromoCod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromoCodRepository extends JpaRepository<PromoCod, String> {
}
