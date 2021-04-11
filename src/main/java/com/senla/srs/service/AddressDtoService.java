package com.senla.srs.service;

import com.senla.srs.dto.db.AddressDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface AddressDtoService {
    Optional<AddressDTO> retrieveAddressDtoById(Long id);
}
