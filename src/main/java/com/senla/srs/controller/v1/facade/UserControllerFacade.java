package com.senla.srs.controller.v1.facade;

import com.senla.srs.dto.user.UserDTO;
import com.senla.srs.dto.user.UserFullResponseDTO;
import com.senla.srs.dto.user.UserRequestDTO;
import com.senla.srs.mapper.UserFullResponseMapper;
import com.senla.srs.mapper.UserRequestMapper;
import com.senla.srs.model.UserStatus;
import com.senla.srs.model.security.Role;
import com.senla.srs.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Slf4j
@Controller
public class UserControllerFacade extends AbstractFacade implements
        EntityControllerFacade<UserDTO, UserRequestDTO, UserFullResponseDTO, Long> {

    private final UserFullResponseMapper userFullResponseMapper;
    private final UserRequestMapper userRequestMapper;

    private static final String USER_NOT_FOUND = "A User with this id not found";
    private static final String ACCESS_FORBIDDEN = "Access forbidden";
    private static final String RE_AUTH = "To change this User reAuthorize";
    private static final String CHANGE_DEFAULT_FIELD = "To top up your balance, obtain administrator rights or " +
            "deactivate a profile, contact the administrator";

    public UserControllerFacade(UserService userService, UserFullResponseMapper userFullResponseMapper, UserRequestMapper userRequestMapper) {
        super(userService);
        this.userFullResponseMapper = userFullResponseMapper;
        this.userRequestMapper = userRequestMapper;
    }

    @Override
    public Page<UserFullResponseDTO> getAll(Integer page, Integer size, String sort, User userSecurity) {
        return isAdmin(userSecurity)
                ? userFullResponseMapper.mapPageToDtoPage(userService.retrieveAllUsers(page, size, sort))

                : userFullResponseMapper.mapPageToDtoPage(
                userService.retrieveAllUsersByEmail(userSecurity.getUsername(), page, size, sort));
    }

    @Override
    public ResponseEntity<?> getById(Long id, User userSecurity) {
        if (isThisUserById(userSecurity, id) || isAdmin(userSecurity)) {
            Optional<com.senla.srs.model.User> optionalUser = userService.retrieveUserById(id);

            return optionalUser.isPresent()
                    ? ResponseEntity.ok(userFullResponseMapper.toDto(optionalUser.get()))
                    : new ResponseEntity<>(USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(ACCESS_FORBIDDEN, HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public ResponseEntity<?> createOrUpdate(UserRequestDTO requestDTO, User userSecurity) {
        Optional<com.senla.srs.model.User> optionalExistUser = userService.retrieveUserByEmail(requestDTO.getEmail());

        if (userSecurity != null) {
            if (isAdmin(userSecurity)) {
                return save(requestDTO);
            } else {
                if (optionalExistUser.isEmpty()) {
                    return constrainCreate(requestDTO);
                } else {
                    if (isThisUserByEmail(userSecurity, requestDTO.getEmail())) {
                        return update(requestDTO, optionalExistUser.get());
                    } else {
                        return new ResponseEntity<>(RE_AUTH, HttpStatus.FORBIDDEN);
                    }
                }
            }
        } else if (optionalExistUser.isEmpty()) {
            return constrainCreate(requestDTO);
        } else {
            return new ResponseEntity<>(RE_AUTH, HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        try {
            userService.deleteById(id);
            return new ResponseEntity<>("User with this id was deleted", HttpStatus.ACCEPTED);
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage(), USER_NOT_FOUND);
            return new ResponseEntity<>(USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    private ResponseEntity<?> constrainCreate(UserRequestDTO userRequestDTO) {
        return isValidDtoToConstrainCreate(userRequestDTO)
                ? save(userRequestDTO)
                : new ResponseEntity<>(CHANGE_DEFAULT_FIELD, HttpStatus.FORBIDDEN);
    }

    private boolean isValidDtoToConstrainCreate(UserRequestDTO userRequestDTO) {
        return userRequestDTO.getStatus() == UserStatus.ACTIVE &&
                userRequestDTO.getRole() == Role.USER &&
                userRequestDTO.getBalance() == 0;
    }

    private ResponseEntity<?> update(UserRequestDTO userRequestDTO, com.senla.srs.model.User existUser) {
        return isValidDtoToUpdate(userRequestDTO, existUser)
                ? save(userRequestDTO)
                : new ResponseEntity<>(CHANGE_DEFAULT_FIELD, HttpStatus.FORBIDDEN);
    }

    private boolean isValidDtoToUpdate(UserRequestDTO userRequestDTO, com.senla.srs.model.User existUser) {
        return userRequestDTO.getStatus() == existUser.getStatus() &&
                userRequestDTO.getRole() == existUser.getRole() &&
                userRequestDTO.getBalance().equals(existUser.getBalance());
    }

    private ResponseEntity<?> save(UserRequestDTO userRequestDTO) {
        com.senla.srs.model.User user = userService.save(userRequestMapper.toEntity(userRequestDTO));
        return ResponseEntity.ok(userFullResponseMapper.toDto(user));
    }
}
