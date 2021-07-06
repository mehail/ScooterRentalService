package com.senla.srs.web.controller.v1.facade;

import com.senla.srs.core.dto.user.UserDTO;
import com.senla.srs.core.dto.user.UserFullResponseDTO;
import com.senla.srs.core.dto.user.UserRequestDTO;
import com.senla.srs.core.entity.User;
import com.senla.srs.core.exception.NotFoundEntityException;
import com.senla.srs.core.mapper.UserFullResponseMapper;
import com.senla.srs.core.mapper.UserRequestMapper;
import com.senla.srs.core.security.JwtTokenData;
import com.senla.srs.core.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import java.util.Optional;

@Controller
public class UserControllerFacade extends AbstractFacade implements
        EntityControllerFacade<UserDTO, UserRequestDTO, UserFullResponseDTO, Long> {

    private static final String ACCESS_FORBIDDEN = "Access forbidden";
    private static final String RE_AUTH = "To change this User reAuthorize";
    private final UserService userService;
    private final UserFullResponseMapper userFullResponseMapper;
    private final UserRequestMapper userRequestMapper;
    private final Validator userRequestNewDTOValidator;
    private final Validator userRequestExistDTOValidator;
    private final Validator userRequestDTOFromAdminValidator;

    public UserControllerFacade(UserFullResponseMapper userFullResponseMapper,
                                UserRequestMapper userRequestMapper,
                                UserService userService,
                                JwtTokenData jwtTokenData,
                                @Qualifier("userRequestExistDTOValidator") Validator userRequestExistDTOValidator,
                                @Qualifier("userRequestDTOFromAdminValidator") Validator userRequestDTOFromAdminValidator,
                                @Qualifier("userRequestNewDTOValidator") Validator userRequestNewDTOValidator) {
        super(jwtTokenData);
        this.userFullResponseMapper = userFullResponseMapper;
        this.userRequestMapper = userRequestMapper;
        this.userService = userService;
        this.userRequestNewDTOValidator = userRequestNewDTOValidator;
        this.userRequestExistDTOValidator = userRequestExistDTOValidator;
        this.userRequestDTOFromAdminValidator = userRequestDTOFromAdminValidator;
    }

    @Override
    public Page<UserFullResponseDTO> getAll(Integer page, Integer size, String sort, String token) {
        return isAdmin(token)
                ? userFullResponseMapper.mapPageToDtoPage(userService.retrieveAllUsers(page, size, sort))

                : userFullResponseMapper.mapPageToDtoPage(
                userService.retrieveAllUsersByEmail(getAuthUserEmail(token), page, size, sort));
    }

    @Override
    public ResponseEntity<?> getById(Long id, String token) throws NotFoundEntityException {

        if (isThisUserById(token, id) || isAdmin(token)) {
            return new ResponseEntity<>(userService.retrieveUserById(id)
                    .map(userFullResponseMapper::toDto)
                    .orElseThrow(() -> new NotFoundEntityException(User.class, id)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ACCESS_FORBIDDEN, HttpStatus.FORBIDDEN);
        }

    }

    @Override
    public ResponseEntity<?> createOrUpdate(UserRequestDTO requestDTO, BindingResult bindingResult, String token) {
        Optional<User> optionalExistUser = userService.retrieveUserByEmail(requestDTO.getEmail());

        if (token == null || token.isEmpty()) {

            if (optionalExistUser.isEmpty()) {
                userRequestNewDTOValidator.validate(requestDTO, bindingResult);
                return save(requestDTO, bindingResult);
            } else {
                return new ResponseEntity<>(RE_AUTH, HttpStatus.FORBIDDEN);
            }

        } else {

            if (isAdmin(token)) {
                userRequestDTOFromAdminValidator.validate(requestDTO, bindingResult);
                return save(requestDTO, bindingResult);
            } else {

                if (isThisUserByEmail(token, requestDTO.getEmail())) {
                    userRequestExistDTOValidator.validate(requestDTO, bindingResult);
                    return save(requestDTO, bindingResult);
                } else {
                    return new ResponseEntity<>(RE_AUTH, HttpStatus.FORBIDDEN);
                }

            }

        }

    }

    @Override
    public ResponseEntity<?> delete(Long id, String token) {
        userService.deleteById(id);
        return new ResponseEntity<>("User with this id was deleted", HttpStatus.ACCEPTED);
    }

    @Override
    public Long getExistEntityId(UserRequestDTO dto) {
        return userService.retrieveUserByEmail(dto.getEmail())
                .map(User::getId)
                .orElse(null);
    }

    private ResponseEntity<?> save(UserRequestDTO userRequestDTO, BindingResult bindingResult) {

        if (!bindingResult.hasErrors()) {
            var user = userService.save(userRequestMapper.toEntity(userRequestDTO, getExistEntityId(userRequestDTO)));
            return new ResponseEntity<>(userFullResponseMapper.toDto(user), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

    }

}
