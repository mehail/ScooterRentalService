package com.senla.srs.dto.db;

import com.senla.srs.dto.AbstractDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Data
//@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "addresses")
public class AddressDTO extends AbstractDTO {
    @Id
    @NonNull
    private Long id;
    @NonNull
    private String name;
    @ManyToOne
    @JoinColumn(name = "city_id")
    private CityDTO city;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddressDTO)) return false;
        if (!super.equals(o)) return false;

        AddressDTO that = (AddressDTO) o;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getId().hashCode();
        return result;
    }
}
