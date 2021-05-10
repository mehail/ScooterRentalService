package com.senla.srs.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Data
@Table(name = "season_tickets")
public class SeasonTicket extends AbstractEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    @Column(name = "user_id")
    private Long userId;
    @NonNull
    @ManyToOne
    @JoinColumn(name = "scooter_type_id")
    private ScooterType scooterType;
    @NonNull
    private Integer price;
    @NonNull
    @Column(name = "remaining_time")
    private Integer remainingTime;
    @NonNull
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "expired_date")
    private LocalDate expiredDate;
    @Column(name = "available_for_use")
    @NonNull
    private Boolean availableForUse;

}
