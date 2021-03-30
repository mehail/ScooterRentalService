package com.senla.srs.repository;

import com.senla.srs.model.RentalSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalSessionRepository extends JpaRepository<RentalSession, Long> {
    List<RentalSession> findAllByUserId(Long user_id);
}
