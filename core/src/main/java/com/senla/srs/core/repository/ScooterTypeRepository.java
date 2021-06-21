package com.senla.srs.core.repository;

import com.senla.srs.core.entity.ScooterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScooterTypeRepository extends JpaRepository<ScooterType, Long> {

    Optional<ScooterType> findByModel(String model);

}
