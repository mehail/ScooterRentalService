package com.senla.srs.model.entity;

import com.senla.srs.model.entity.security.Role;
import com.senla.srs.model.entity.security.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String email;
    @NonNull
    private String password;
    @NonNull
    @Column(name = "first_name")
    private String firstName;
    @NonNull
    @Column(name = "last_name")
    private String lastName;
    @NonNull
    @Enumerated(EnumType.ORDINAL)
    private Role role;
    @NonNull
    @Enumerated(EnumType.ORDINAL)
    private Status status;
    @NonNull
    private Integer balance;

    public User() {
    }
}
