package com.senla.srs.model.entity.test;

import com.senla.srs.model.entity.MyUser;

import javax.persistence.*;
import java.time.LocalDate;

//@Data
//@Entity
//@Table(name = "rental_sessions")
public class RentalSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private MyUser myUser;
    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name = "scooter_serial_number")
    private Scooter scooter;
    private Integer rate;
    private LocalDate begin;
    private LocalDate end;
    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name = "season_ticket_id")
    private SeasonTicket seasonTicket;
}
