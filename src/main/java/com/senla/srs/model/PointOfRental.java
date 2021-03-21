package com.senla.srs.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;

@Data
@AllArgsConstructor
@Entity
@Table(name = "point_of_rentals")
public class PointOfRental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String name;
    @NonNull
    @Column(name = "cities_id")
    private String city;
    @NonNull
    private String address;
    @NonNull
    private Boolean available;

    public PointOfRental() {
    }

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
