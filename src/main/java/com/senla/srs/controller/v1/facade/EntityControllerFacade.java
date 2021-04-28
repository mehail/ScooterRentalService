package com.senla.srs.controller.v1.facade;

import com.senla.srs.exception.NotFoundEntityException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface EntityControllerFacade<D, Q extends D, S extends D, T> {
    Page<S> getAll(Integer page, Integer size, String sort,
                   org.springframework.security.core.userdetails.User userSecurity);
    ResponseEntity<?> getById(T id, org.springframework.security.core.userdetails.User userSecurity);
    ResponseEntity<?> createOrUpdate(Q requestDTO, org.springframework.security.core.userdetails.User userSecurity) throws NotFoundEntityException;
    ResponseEntity<?> delete(T id);
}
