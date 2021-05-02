package com.senla.srs.service;

import com.senla.srs.dto.db.AddressDTO;

import java.util.Optional;

public interface AddressDtoService {
    Optional<AddressDTO> retrieveAddressDtoById(Long id);
}
