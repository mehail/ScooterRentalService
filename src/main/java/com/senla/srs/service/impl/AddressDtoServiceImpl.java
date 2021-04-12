package com.senla.srs.service.impl;

import com.senla.srs.dto.db.AddressDTO;
import com.senla.srs.repository.AddressDtoRepository;
import com.senla.srs.service.AddressDtoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AddressDtoServiceImpl implements AddressDtoService {
    private final AddressDtoRepository addressDtoRepository;

    @Override
    public Optional<AddressDTO> retrieveAddressDtoById(Long id) {
        return addressDtoRepository.findById(id);
    }
}
