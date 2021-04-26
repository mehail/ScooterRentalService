package com.senla.srs.controller.v1.facade;

import com.senla.srs.dto.promocod.PromoCodDTO;
import com.senla.srs.mapper.PromoCodMapper;
import com.senla.srs.model.PromoCod;
import com.senla.srs.service.PromoCodService;
import com.senla.srs.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Controller
public class PromoCodControllerFacade extends AbstractFacade implements
        EntityControllerFacade<PromoCodDTO, PromoCodDTO, PromoCodDTO, String> {

    private final PromoCodService promoCodService;
    private final PromoCodMapper promoCodMapper;

    private static final String PROMO_COD_NOT_FOUND = "No PromoCod with this name found";

    public PromoCodControllerFacade(UserService userService, PromoCodService promoCodService, PromoCodMapper promoCodMapper) {
        super(userService);
        this.promoCodService = promoCodService;
        this.promoCodMapper = promoCodMapper;
    }

    @Override
    public Page<PromoCodDTO> getAll(Integer page, Integer size, String sort, User userSecurity) {
        return promoCodMapper.mapPageToDtoPage(promoCodService.retrieveAllPromoCods(page, size, sort));
    }

    @Override
    public ResponseEntity<?> getById(String name, User userSecurity) {
        Optional<PromoCod> optionalPromoCod = promoCodService.retrievePromoCodByName(name);

        return optionalPromoCod.isPresent()
                ? ResponseEntity.ok(promoCodMapper.toDto(optionalPromoCod.get()))
                : new ResponseEntity<>(PROMO_COD_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<?> createOrUpdate(PromoCodDTO promoCodDTO, User userSecurity) {
        return isValidDate(promoCodDTO)
                ? create(promoCodDTO)
                : new ResponseEntity<>("The start and end dates of the PromoCod are not correct", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<?> delete(String name) {
        try {
            promoCodService.deleteById(name);
            return new ResponseEntity<>("PromoCod with this serial number was deleted", HttpStatus.ACCEPTED);
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage(), PROMO_COD_NOT_FOUND);
            return new ResponseEntity<>(PROMO_COD_NOT_FOUND, HttpStatus.FORBIDDEN);
        }
    }

    private boolean isValidDate(PromoCodDTO promoCodDTO) {
        LocalDate startDate = promoCodDTO.getStartDate();
        LocalDate expiredDate = promoCodDTO.getExpiredDate();

        return expiredDate == null || startDate.isBefore(expiredDate);
    }

    private ResponseEntity<?> create(PromoCodDTO promoCodDTO) {
        PromoCod promoCod = promoCodService.save(promoCodMapper.toEntity(promoCodDTO));
        return ResponseEntity.ok(promoCodMapper.toDto(promoCod));
    }
}
