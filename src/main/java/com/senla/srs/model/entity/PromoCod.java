package com.senla.srs.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "promo_cods")
public class PromoCod {
    @Id
    private String name;
    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name = "rental_session_id")
    private RentalSession rentalSession;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "expired_date")
    private LocalDate expiredDate;
    @Column(name = "discount_percentage")
    private Integer discountPercentage;
    @Column(name = "bonus_point")
    private Integer bonusPoint;
}
