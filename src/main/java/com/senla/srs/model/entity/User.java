package com.senla.srs.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //ToDo: whether it is necessary to specify the annotation @NotNull
//    @ManyToOne
//    @JoinColumn(name = "role_id")
//    private Role role;

    private String login;

    //ToDo: Rewrite to use hash
    private String password;

    //ToDo: Whether it is necessary to specify the length of the field?
    @Column(name = "first_name", length = 64)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private LocalDate birthday;

    private String email;

    private int balance;

    public User() {
    }

    public User(String login, String password, String firstName, String lastName, LocalDate birthday,
                String email, int balance) {

//    public User(Role role, String login, String password, String firstName, String lastName, LocalDate birthday,
//                String email, int balance) {
//        this.role = role;
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.email = email;
        this.balance = balance;
    }
}
