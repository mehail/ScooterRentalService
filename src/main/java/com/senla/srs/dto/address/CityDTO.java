package com.senla.srs.dto.address;

import com.senla.srs.dto.AbstractDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Table(name = "cities")
public class CityDTO extends AbstractDTO {
    @Id
    @NonNull
    private Long id;
    @NonNull
    private String name;
    @NonNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "country_id")
    private CountryDTO country;
}
