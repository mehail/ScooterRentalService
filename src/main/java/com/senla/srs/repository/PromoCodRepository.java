package com.senla.srs.repository;

import com.senla.srs.entity.PromoCod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromoCodRepository extends JpaRepository<PromoCod, String> {

    Optional<PromoCod> findByName(String name);

}
