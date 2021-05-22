package com.senla.srs.repository;

import com.senla.srs.dto.scooter.type.MakerDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MakerDtoRepository extends JpaRepository<MakerDTO, Long> {
}
