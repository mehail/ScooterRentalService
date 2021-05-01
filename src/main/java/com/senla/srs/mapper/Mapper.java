package com.senla.srs.mapper;

import com.senla.srs.dto.AbstractDTO;
import com.senla.srs.entity.AbstractEntity;

public interface Mapper<E extends AbstractEntity, D extends AbstractDTO> {
    E toEntity(D dto);
    D toDto(E entity);
}
