package com.senla.srs.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;

@Data
@AllArgsConstructor
@Entity
@Table(name = "scooter_types")
public class ScooterType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String model;
    @NonNull
    @Column(name = "maker_id")
    private String maker;
    @NonNull
    private Double maxSpeed;
    @NonNull
    @Column(name = "price_per_minute")
    private Integer pricePerMinute;

    public ScooterType() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScooterType)) return false;

        ScooterType that = (ScooterType) o;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
