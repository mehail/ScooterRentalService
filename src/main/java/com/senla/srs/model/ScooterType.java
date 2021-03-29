package com.senla.srs.model;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
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
}
