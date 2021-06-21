package com.senla.srs.core.repository;

import com.senla.srs.core.dto.geo.GisPointDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GisPointDtoRepository extends JpaRepository<GisPointDTO, Long> {
}
