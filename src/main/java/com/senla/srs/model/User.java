package com.senla.srs.model;

import com.senla.srs.model.security.Role;
import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
@Table(name = "users")
public class User extends AbstractEntity{
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
    private UserStatus status;
    @NonNull
    private Integer balance;
}
