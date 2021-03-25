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
@Table(name = "scooter_types")
public class ScooterType extends AbstractEntity{
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
