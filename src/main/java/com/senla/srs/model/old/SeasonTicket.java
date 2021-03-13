package com.senla.srs.model.old;

import com.senla.srs.model.User;

import javax.persistence.*;
import java.time.LocalDate;

//@Entity
//@Data
//@Table(name = "season_tickets")
public class SeasonTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User myUser;
    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name = "scooter_type_id")
    private ScooterType scooterType;
    private Integer price;
    @Column(name = "remaining_time")
    private Integer remainingTime;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "expired_date")
    private LocalDate expiredDate;
    @Column(name = "available_for_use")
    private Boolean availableForUse;
}
