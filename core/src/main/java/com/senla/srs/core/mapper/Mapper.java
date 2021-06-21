package com.senla.srs.core.mapper;

import com.senla.srs.core.dto.AbstractDTO;
import com.senla.srs.core.entity.AbstractEntity;

public interface Mapper<E extends AbstractEntity, D extends AbstractDTO> {

    E toEntity(D dto);
    D toDto(E entity);

}
