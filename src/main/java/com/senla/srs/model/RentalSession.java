package com.senla.srs.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

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
    @ManyToOne(optional=false)
    @JoinColumn(name = "user_id")
    private User user;
    @NonNull
    @ManyToOne(optional=false)
    @JoinColumn(name = "scooter_serial_number")
    private Scooter scooter;
    @NonNull
    private Integer rate;
    @NonNull
    private LocalDate begin;
    private LocalDate end;
    @ManyToOne(optional=false)
    @JoinColumn(name = "season_ticket_id")
    private SeasonTicket seasonTicket;
    //ToDo Refactor for @OneToOne
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_session_id")
    private List<PromoCod> promoCods;
}
