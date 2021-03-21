package com.senla.srs.model;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;
import java.time.LocalDate;

@Data
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
}
