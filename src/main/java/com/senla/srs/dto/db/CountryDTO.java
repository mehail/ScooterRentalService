package com.senla.srs.dto.db;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "countries")
public class CountryDTO implements Serializable {
    @Id
    private Long id;
    private String name;
}
