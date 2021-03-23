package com.senla.srs.mapper;

import com.senla.srs.dto.AbstractDto;
import com.senla.srs.model.AbstractEntity;

public interface Mapper<E extends AbstractEntity, D extends AbstractDto> {

    E toEntity(D dto);

    D toDto(E entity);
}
