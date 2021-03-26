package com.senla.srs.dto.db;

import com.senla.srs.dto.AbstractDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "cities")
public class CityDTO extends AbstractDTO {
    @Id
    @NonNull
    private Long id;
    @NonNull
    private String name;
    @ManyToOne
    @JoinColumn(name = "country_id")
    private CountryDTO country;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CityDTO)) return false;
        if (!super.equals(o)) return false;

        CityDTO cityDTO = (CityDTO) o;

        return getId().equals(cityDTO.getId());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getId().hashCode();
        return result;
    }
}
