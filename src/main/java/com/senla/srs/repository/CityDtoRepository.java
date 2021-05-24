package com.senla.srs.repository;

import com.senla.srs.dto.geo.CityDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityDtoRepository extends JpaRepository<CityDTO, Long> {
}
