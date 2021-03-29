package com.senla.srs.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Table(name = "rental_sessions")
public class RentalSession extends AbstractEntity{
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
}
