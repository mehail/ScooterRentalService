package com.senla.srs.core.repository;

import com.senla.srs.core.dto.scooter.type.MakerDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MakerDtoRepository extends JpaRepository<MakerDTO, Long> {
}
