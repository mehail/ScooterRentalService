package com.senla.srs.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "scooter_types")
public class ScooterType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String model;
    @Column(name = "maker_id")
    private String maker;
    private Double maxSpeed;
    @Column(name = "price_per_minute")
    private Integer pricePerMinute;
}
