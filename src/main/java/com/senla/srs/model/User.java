package com.senla.srs.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

//@Component
@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Enumerated(EnumType.ORDINAL)
    private Role role;
    @Enumerated(EnumType.ORDINAL)
    private Status status;
    private Integer balance;

    public User() {
    }
}
