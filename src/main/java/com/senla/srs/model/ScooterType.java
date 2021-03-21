package com.senla.srs.model;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;

@Data
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
}
