package com.senla.srs.model.repository;

import com.senla.srs.model.entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository1 extends JpaRepository<MyUser, Long> {
}
