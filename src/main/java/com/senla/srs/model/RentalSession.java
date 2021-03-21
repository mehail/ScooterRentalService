package com.senla.srs.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Entity
@Table(name = "rental_sessions")
public class RentalSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
    @NonNull
    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name = "scooter_serial_number")
    private Scooter scooter;
    @NonNull
    private Integer rate;
    @NonNull
    private LocalDate begin;
    private LocalDate end;
    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name = "season_ticket_id")
    private SeasonTicket seasonTicket;

    public RentalSession() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RentalSession)) return false;

        RentalSession that = (RentalSession) o;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
