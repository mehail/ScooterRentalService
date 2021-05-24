package com.senla.srs.dto.geo;

import com.senla.srs.dto.AbstractDTO;
import liquibase.pro.packaged.C;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Table(name = "gis_points")
public class GisPointDTO extends AbstractDTO {

    @Id
    @NonNull
    @Min(value = 1, message = "ID must be at least 1")
    private Long id;
    @NonNull
    @Length(min = 1, max = 64, message = "Name must be between 1 and 64 characters")
    private String address;
    @NonNull
    @Min(value = 1, message = "PointOfRentalID must be at least 1")
    @Column(name = "gis_point_of_rental_id")
    private Long pointOfRentalId;
    @NonNull
    @Range(min = -180, max = 180, message = "Longitude must be between -180 and 180")
    private Double longitude;
    @NonNull
    @Range(min = -90, max = 90, message = "Latitude must be between -90 and 90")
    private Double latitude;

}
