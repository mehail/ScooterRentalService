package com.senla.srs.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"user", "scooter", "begin"}, callSuper = false)
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
    private Integer rate;
    @NonNull
    private LocalDateTime begin;
    private LocalDateTime end;
    @ManyToOne
    @JoinColumn(name = "season_ticket_id")
    private SeasonTicket seasonTicket;
    @ManyToOne
    @JoinColumn(name = "promo_cod_name")
    private PromoCod promoCod;
}
