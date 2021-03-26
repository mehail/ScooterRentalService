package com.senla.srs.dto.db;

import com.senla.srs.dto.AbstractDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Entity
@Table(name = "countries")
public class CountryDTO extends AbstractDTO {
    @Id
    @NonNull
    private Long id;
    @NonNull
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CountryDTO)) return false;
        if (!super.equals(o)) return false;

        CountryDTO that = (CountryDTO) o;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getId().hashCode();
        return result;
    }
}
