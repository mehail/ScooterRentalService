package com.senla.srs.core.entity;

import com.senla.srs.core.dto.geo.CityDTO;
import com.senla.srs.core.dto.geo.GisPointDTO;
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
    @ManyToOne(optional = false)
    @JoinColumn(name = "city_id")
    private CityDTO cityDTO;
    @NonNull
    private Boolean available;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_of_rental_id")
    private List<Scooter> scooters;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "gis_point_of_rental_id")
    private List<GisPointDTO> rollingTrack;

}
