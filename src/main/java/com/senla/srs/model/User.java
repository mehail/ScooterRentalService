package com.senla.srs.model;

import com.senla.srs.model.security.Role;
import com.senla.srs.model.security.Status;
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
    @Enumerated(value = EnumType.STRING)
    private Role role;
    @Enumerated(value = EnumType.STRING)
    private Status status;
    private int balance;

    public User() {

    }
}
