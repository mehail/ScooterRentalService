package com.senla.srs.model;

import com.senla.srs.dto.address.AddressDTO;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Table(name = "point_of_rentals")
public class PointOfRental extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String name;
    @NonNull
    @OneToOne(optional = false)
    @JoinColumn(name = "address_id")
    private AddressDTO address;
    @NonNull
    private Boolean available;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_of_rental_id")
    private List<Scooter> scooters;
}
