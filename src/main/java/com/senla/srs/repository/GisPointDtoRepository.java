package com.senla.srs.repository;

import com.senla.srs.dto.geo.GisPointDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GisPointDtoRepository extends JpaRepository<GisPointDTO, Long> {
}
