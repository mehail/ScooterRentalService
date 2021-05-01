package com.senla.srs.mapper;

import com.senla.srs.dto.AbstractDTO;
import com.senla.srs.entity.AbstractEntity;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractMapperWithPagination<E extends AbstractEntity, D extends AbstractDTO> extends AbstractMapper<E, D>{

    AbstractMapperWithPagination(Class<E> entityClass, Class<D> dtoClass) {
        super(entityClass, dtoClass);
    }

    public Page<D> mapPageToDtoPage(Page<E> page) {
        List<D> seasonTicketFullResponseDTOS = page.getContent().stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(seasonTicketFullResponseDTOS, page.getPageable(), page.getTotalElements());
    }
}
