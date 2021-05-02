package com.senla.srs.dto.rentalsession;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.senla.srs.dto.AbstractDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"begin", "end"}, callSuper = false)
public class RentalSessionDTO extends AbstractDTO {
    @NonNull
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    LocalDateTime begin;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    LocalDateTime end;
}
