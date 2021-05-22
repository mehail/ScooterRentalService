package com.senla.srs.repository;

import com.senla.srs.dto.geo.AddressDTO;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressDtoRepository extends JpaRepository<AddressDTO, Long> {

    @NonNull
    Optional<AddressDTO> findById(@NonNull Long id);

}
