package com.senla.srs.repository;

import com.senla.srs.model.SeasonTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeasonTicketRepository extends JpaRepository<SeasonTicket, Long> {
}
