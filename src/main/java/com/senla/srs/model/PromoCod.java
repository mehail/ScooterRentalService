package com.senla.srs.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Entity
@Table(name = "promo_cods")
public class PromoCod {
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

    public PromoCod() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PromoCod)) return false;

        PromoCod promoCod = (PromoCod) o;

        return getName().equals(promoCod.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}
