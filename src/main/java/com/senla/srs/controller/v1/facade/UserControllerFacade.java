package com.senla.srs.controller.v1.facade;

import com.senla.srs.dto.user.UserDTO;
import com.senla.srs.dto.user.UserFullResponseDTO;
import com.senla.srs.dto.user.UserRequestDTO;
import com.senla.srs.exception.NotFoundEntityException;
import com.senla.srs.mapper.UserFullResponseMapper;
import com.senla.srs.mapper.UserRequestMapper;
import com.senla.srs.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import java.util.Optional;

@Slf4j
@Controller
public class UserControllerFacade extends AbstractFacade implements
        EntityControllerFacade<UserDTO, UserRequestDTO, UserFullResponseDTO, Long> {

    private static final String ACCESS_FORBIDDEN = "Access forbidden";
    private static final String RE_AUTH = "To change this User reAuthorize";
    private static final String CHANGE_DEFAULT_FIELD = "To top up your balance, obtain administrator rights or " +
            "deactivate a profile, contact the administrator";
    private final UserFullResponseMapper userFullResponseMapper;
    private final UserRequestMapper userRequestMapper;
    private final Validator validator;

    public UserControllerFacade(UserService userService,
                                UserFullResponseMapper userFullResponseMapper,
                                UserRequestMapper userRequestMapper,
                                @Qualifier("userRequestValidator") Validator validator) {
        super(userService);
        this.userFullResponseMapper = userFullResponseMapper;
        this.userRequestMapper = userRequestMapper;
        this.validator = validator;
    }

    @Override
    public Page<UserFullResponseDTO> getAll(Integer page,
                                            Integer size,
                                            String sort,
                                            org.springframework.security.core.userdetails.User userSecurity) {
        return isAdmin(userSecurity)
                ? userFullResponseMapper.mapPageToDtoPage(userService.retrieveAllUsers(page, size, sort))

                : userFullResponseMapper.mapPageToDtoPage(
                userService.retrieveAllUsersByEmail(userSecurity.getUsername(), page, size, sort));
    }

    @Override
    public ResponseEntity<?> getById(Long id, org.springframework.security.core.userdetails.User userSecurity)
            throws NotFoundEntityException {

        if (isThisUserById(userSecurity, id) || isAdmin(userSecurity)) {
            return new ResponseEntity<>(userService.retrieveUserById(id)
                    .map(userFullResponseMapper::toDto)
                    .orElseThrow(() -> new NotFoundEntityException("User")), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ACCESS_FORBIDDEN, HttpStatus.FORBIDDEN);
        }

    }

    //ToDo Выбрасывать исключение с FORBIDDEN
    public ResponseEntity<?> createOrUpdate(UserRequestDTO requestDTO,
                                            BindingResult bindingResult,
                                            org.springframework.security.core.userdetails.User userSecurity) {
        Optional<com.senla.srs.model.User> optionalExistUser = userService.retrieveUserByEmail(requestDTO.getEmail());

        if (userSecurity != null) {
            if (isAdmin(userSecurity)) {
                //ToDo test methods
                return constrainCreate(requestDTO, bindingResult);
//                return save(requestDTO);
            } else {
                if (optionalExistUser.isEmpty()) {
                    return constrainCreate(requestDTO, bindingResult);
                } else {
                    if (isThisUserByEmail(userSecurity, requestDTO.getEmail())) {
                        return update(requestDTO, optionalExistUser.get());
                    } else {
                        return new ResponseEntity<>(RE_AUTH, HttpStatus.FORBIDDEN);
                    }
                }
            }
        } else if (optionalExistUser.isEmpty()) {
            return constrainCreate(requestDTO, bindingResult);
        } else {
            return new ResponseEntity<>(RE_AUTH, HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        userService.deleteById(id);
        return new ResponseEntity<>("User with this id was deleted", HttpStatus.ACCEPTED);
    }

    private ResponseEntity<?> constrainCreate(UserRequestDTO userRequestDTO, BindingResult bindingResult) {
        validator.validate(userRequestDTO, bindingResult);

        if (!bindingResult.hasErrors()) {
            return save(userRequestDTO);
        } else {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.FORBIDDEN);
        }
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
