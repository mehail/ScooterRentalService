package com.senla.srs.repository;

import com.senla.srs.entity.PointOfRental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PointOfRentalRepository extends JpaRepository<PointOfRental, Long> {

    Optional<PointOfRental> findByName(String name);

}
