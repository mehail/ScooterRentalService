package com.senla.srs.repository;

import com.senla.srs.model.ScooterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScooterTypeRepository extends JpaRepository<ScooterType, Long> {
}
