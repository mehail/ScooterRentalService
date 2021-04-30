package com.senla.srs.dto.db;

import com.senla.srs.dto.AbstractDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Table(name = "cities")
public class CityDTO extends AbstractDTO {
    @Id
    @NonNull
    @Min(value = 1, message = "ID must be at least 1")
    private Long id;
    @NonNull
    @Length(min = 1, max = 64, message = "Name must be between 1 and 64 characters")
    private String name;
    @NonNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "country_id")
    private CountryDTO country;
}
