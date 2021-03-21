package com.senla.srs.repository;

import com.senla.srs.model.PointOfRental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointOfRentalRepository extends JpaRepository<PointOfRental, Long> {
}
