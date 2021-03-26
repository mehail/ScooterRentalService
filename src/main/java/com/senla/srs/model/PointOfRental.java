package com.senla.srs.model;

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
@SecondaryTable(name = "cities", pkJoinColumns = {@PrimaryKeyJoinColumn(name = "id")})
public class PointOfRental extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String name;
    //ToDo подтягивать страну и город
    @NonNull
    @Column(table = "cities")
    private String city;
    @NonNull
    private String address;
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
