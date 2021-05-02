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
import com.senla.srs.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import java.util.Optional;

@Slf4j
@Controller
public class UserControllerFacade extends AbstractFacade implements
        EntityControllerFacade<UserDTO, UserRequestDTO, UserFullResponseDTO, Long> {

    private static final String ACCESS_FORBIDDEN = "Access forbidden";
    private static final String RE_AUTH = "To change this User reAuthorize";
    private final UserFullResponseMapper userFullResponseMapper;
    private final UserRequestMapper userRequestMapper;
    private final Validator<User, UserRequestDTO> validator;

    public UserControllerFacade(UserFullResponseMapper userFullResponseMapper,
                                UserRequestMapper userRequestMapper,
                                UserService userService,
                                JwtTokenData jwtTokenData,
                                Validator<User, UserRequestDTO> validator) {
        super(userService, jwtTokenData);
        this.userFullResponseMapper = userFullResponseMapper;
        this.userRequestMapper = userRequestMapper;
        this.validator = validator;
    }

    @Override
    public Page<UserFullResponseDTO> getAll(Integer page, Integer size, String sort, String token) {
        return isAdmin(token)
                ? userFullResponseMapper.mapPageToDtoPage(userService.retrieveAllUsers(page, size, sort))

                : userFullResponseMapper.mapPageToDtoPage(
                userService.retrieveAllUsersByEmail(getAuthUserEmail(token), page, size, sort));
    }

    @Override
    public ResponseEntity<?> getById(Long id, String token)
            throws NotFoundEntityException {

        if (isThisUserById(token, id) || isAdmin(token)) {
            return new ResponseEntity<>(userService.retrieveUserById(id)
                    .map(userFullResponseMapper::toDto)
                    .orElseThrow(() -> new NotFoundEntityException("User")), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ACCESS_FORBIDDEN, HttpStatus.FORBIDDEN);
        }

    }

    @Override
    public ResponseEntity<?> createOrUpdate(UserRequestDTO requestDTO, BindingResult bindingResult, String token) {
        Optional<com.senla.srs.entity.User> optionalExistUser = userService.retrieveUserByEmail(requestDTO.getEmail());

        if (token == null || token.isEmpty()) {
            return save(validator.validateNewDto(requestDTO, bindingResult), bindingResult);
        } else {
            if (isAdmin(token)) {
                return save(validator.validateDtoFromAdmin(requestDTO, bindingResult), bindingResult);
            } else {
                if (isThisUserByEmail(token, requestDTO.getEmail())) {
                    return save(validator.validateExistDto(requestDTO, bindingResult, optionalExistUser), bindingResult);
                } else {
                    return new ResponseEntity<>(RE_AUTH, HttpStatus.FORBIDDEN);
                }
            }
        }
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        userService.deleteById(id);
        return new ResponseEntity<>("User with this id was deleted", HttpStatus.ACCEPTED);
    }

    private ResponseEntity<?> save(UserRequestDTO userRequestDTO, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            User user = userService.save(userRequestMapper.toEntity(userRequestDTO));
            return ResponseEntity.ok(userFullResponseMapper.toDto(user));
        } else {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
    }
}
