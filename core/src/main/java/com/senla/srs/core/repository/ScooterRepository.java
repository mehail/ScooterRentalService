package com.senla.srs.core.repository;

import com.senla.srs.core.entity.Scooter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScooterRepository extends JpaRepository<Scooter, String> {
}
