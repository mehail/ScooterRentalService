package com.senla.srs.web.controller.v1.facade;

import com.senla.srs.core.exception.NotFoundEntityException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface EntityControllerFacade<D, Q extends D, S extends D, T> {

    Page<S> getAll(Integer page, Integer size, String sort, String token);

    ResponseEntity<?> getById(T id, String token)
            throws NotFoundEntityException;

    ResponseEntity<?> createOrUpdate(Q requestDTO, BindingResult bindingResult, String token)
            throws NotFoundEntityException;

    ResponseEntity<?> delete(T id, String token) throws NotFoundEntityException;

    T getExistEntityId(Q dto);

}
