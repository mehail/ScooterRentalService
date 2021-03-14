package com.senla.srs.model.old;

import javax.persistence.*;

//@Data
//@Entity
//@Table(name = "scooters")
public class Scooter {
    @Id
    @Column(name = "serial_number")
    private String serialNumber;
    @ManyToOne(optional=false, cascade= CascadeType.ALL)
    @JoinColumn(name = "point_of_rental_id")
    private PointOfRental pointOfRental;
    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name = "scooter_type_id")
    private ScooterType type;
    @Column(name = "time_millage")
    private Integer timeMillage;
}
