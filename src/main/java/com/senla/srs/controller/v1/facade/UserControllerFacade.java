package com.senla.srs.controller.v1.facade;

import com.senla.srs.dto.user.UserDTO;
import com.senla.srs.dto.user.UserFullResponseDTO;
import com.senla.srs.dto.user.UserRequestDTO;
import com.senla.srs.entity.User;
import com.senla.srs.exception.NotFoundEntityException;
import com.senla.srs.mapper.UserFullResponseMapper;
import com.senla.srs.mapper.UserRequestMapper;
import com.senla.srs.security.JwtTokenData;
import com.senla.srs.service.UserService;
import com.senla.srs.validator.UserRequestValidator;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import java.util.Optional;

@Controller
public class UserControllerFacade extends AbstractFacade implements
        EntityControllerFacade<UserDTO, UserRequestDTO, UserFullResponseDTO, Long> {

    private static final String ACCESS_FORBIDDEN = "Access forbidden";
    private static final String RE_AUTH = "To change this User reAuthorize";
    private final UserService userService;
    private final UserFullResponseMapper userFullResponseMapper;
    private final UserRequestMapper userRequestMapper;
    private final UserRequestValidator userRequestValidator;

    public UserControllerFacade(UserFullResponseMapper userFullResponseMapper,
                                UserRequestMapper userRequestMapper,
                                UserRequestValidator userRequestValidator,
                                JwtTokenData jwtTokenData, UserService userService) {
        super(jwtTokenData);
        this.userFullResponseMapper = userFullResponseMapper;
        this.userRequestMapper = userRequestMapper;
        this.userRequestValidator = userRequestValidator;
        this.userService = userService;
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
        Optional<com.senla.srs.entity.User> optionalExistUser = userService.retrieveUserByEmail(requestDTO.getEmail());

        if (token == null || token.isEmpty()) {
            return save(userRequestValidator.validateNewDto(requestDTO, bindingResult), bindingResult);
        } else {

            if (isAdmin(token)) {
                return save(userRequestValidator.validateDtoFromAdmin(requestDTO, bindingResult), bindingResult);
            } else {

                if (isThisUserByEmail(token, requestDTO.getEmail())) {
                    return save(userRequestValidator.validateExistDto(requestDTO, bindingResult, optionalExistUser), bindingResult);
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
