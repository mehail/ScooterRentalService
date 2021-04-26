package com.senla.srs.facade;

import com.senla.srs.dto.user.UserFullResponseDTO;
import com.senla.srs.dto.user.UserRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface UserControllerFacade {
    Page<UserFullResponseDTO> getAll(Integer page, Integer size, String sort,
                                     org.springframework.security.core.userdetails.User userSecurity);
    ResponseEntity<?> getById(Long id, org.springframework.security.core.userdetails.User userSecurity);
    ResponseEntity<?> createOrUpdate(UserRequestDTO userRequestDTO, org.springframework.security.core.userdetails.User userSecurity);
    ResponseEntity<?> delete(Long id);
}
