package com.senla.srs.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"name"}, callSuper = false)
@Entity
@Table(name = "promo_cods")
public class PromoCod extends AbstractEntity{
    @Id
    private String name;
    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name = "rental_session_id")
    private RentalSession rentalSession;
    @NonNull
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "expired_date")
    private LocalDate expiredDate;
    @Column(name = "discount_percentage")
    private Integer discountPercentage;
    @Column(name = "bonus_point")
    private Integer bonusPoint;
}
