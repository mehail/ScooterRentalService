package com.senla.srs.service;

import com.senla.srs.dto.geo.AddressDTO;

import java.util.Optional;

public interface AddressDtoService {

    Optional<AddressDTO> retrieveAddressDtoById(Long id);

}
