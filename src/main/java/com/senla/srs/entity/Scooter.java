package com.senla.srs.entity;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"serialNumber"}, callSuper = false)
@Entity
@Table(name = "scooters")
public class Scooter extends AbstractEntity{

    @Id
    @Column(name = "serial_number")
    private String serialNumber;
    @NonNull
    @Column(name = "point_of_rental_id")
    private Long pointOfRentalId;
    @NonNull
    @ManyToOne(optional=false)
    @JoinColumn(name = "scooter_type_id")
    private ScooterType type;
    @NonNull
    @Column(name = "time_millage")
    private Integer timeMillage;
    @NonNull
    @Enumerated(EnumType.ORDINAL)
    private ScooterStatus status;

}
