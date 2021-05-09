package com.senla.srs.mapper;

import com.senla.srs.dto.promocod.PromoCodDTO;
import com.senla.srs.entity.PromoCod;
import org.springframework.stereotype.Component;

@Component
public class PromoCodMapper extends AbstractMapperWithPagination<PromoCod, PromoCodDTO> {
    public PromoCodMapper() {
        super(PromoCod.class, PromoCodDTO.class);
    }

    @Override
    public PromoCod toEntity(PromoCodDTO dto) {
        PromoCod entity = new PromoCod();
        entity.setName(dto.getName());
        entity.setStartDate(dto.getStartDate());
        entity.setExpiredDate(dto.getExpiredDate());
        entity.setDiscountPercentage(dto.getDiscountPercentage());
        entity.setBonusPoint(dto.getBonusPoint());
        entity.setAvailable(dto.getAvailable());

        return entity;
    }

    @Override
    public PromoCodDTO toDto(PromoCod entity) {
        PromoCodDTO dto = new PromoCodDTO();
        dto.setName(entity.getName());
        dto.setStartDate(entity.getStartDate());
        dto.setExpiredDate(entity.getExpiredDate());
        dto.setDiscountPercentage(entity.getDiscountPercentage());
        dto.setBonusPoint(entity.getBonusPoint());
        dto.setAvailable(entity.getAvailable());

        return dto;
    }
}
