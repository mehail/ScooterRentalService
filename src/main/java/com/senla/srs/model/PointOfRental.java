package com.senla.srs.model;

import com.senla.srs.dto.test.AddressDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "point_of_rentals")
public class PointOfRental extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String name;
    @NonNull
    @OneToOne
    @JoinColumn(name = "address_id")
    private AddressDTO address;
    @NonNull
    private Boolean available;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PointOfRental)) return false;

        PointOfRental that = (PointOfRental) o;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
