package com.senla.srs.core.entity;

import com.senla.srs.core.dto.scooter.type.MakerDTO;
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
    @ManyToOne
    @JoinColumn(name = "maker_id")
    private MakerDTO maker;
    @NonNull
    private Double maxSpeed;
    @NonNull
    @Column(name = "price_per_minute")
    private Integer pricePerMinute;

}
