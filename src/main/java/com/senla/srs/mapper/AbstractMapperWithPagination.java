package com.senla.srs.mapper;

import com.senla.srs.dto.AbstractDTO;
import com.senla.srs.model.AbstractEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractMapperWithPagination<E extends AbstractEntity, D extends AbstractDTO> extends AbstractMapper<E, D>{

    AbstractMapperWithPagination(Class<E> entityClass, Class<D> dtoClass) {
        super(entityClass, dtoClass);
    }

    public Page<D> mapPageToDtoPage(Page<E> seasonTickets) {
        List<D> seasonTicketFullResponseDTOS = seasonTickets.getContent().stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(seasonTicketFullResponseDTOS);
    }
}
