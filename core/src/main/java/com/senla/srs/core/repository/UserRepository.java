package com.senla.srs.core.repository;

import com.senla.srs.core.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findAllByEmail(String email, Pageable paging);

    Optional<User> findByEmail(String email);

}
