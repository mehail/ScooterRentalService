package com.senla.srs.controller.v1.facade;

import com.senla.srs.dto.promocod.PromoCodDTO;
import com.senla.srs.exception.NotFoundEntityException;
import com.senla.srs.mapper.PromoCodMapper;
import com.senla.srs.model.PromoCod;
import com.senla.srs.security.JwtTokenData;
import com.senla.srs.service.PromoCodService;
import com.senla.srs.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;

@Slf4j
@Controller
public class PromoCodControllerFacade extends AbstractFacade implements
        EntityControllerFacade<PromoCodDTO, PromoCodDTO, PromoCodDTO, String> {

    private final PromoCodService promoCodService;
    private final PromoCodMapper promoCodMapper;

    public PromoCodControllerFacade(PromoCodService promoCodService,
                                    PromoCodMapper promoCodMapper,
                                    UserService userService,
                                    JwtTokenData jwtTokenData) {
        super(userService, jwtTokenData);
        this.promoCodService = promoCodService;
        this.promoCodMapper = promoCodMapper;
    }

    @Override
    public Page<PromoCodDTO> getAll(Integer page, Integer size, String sort, String token) {
        return promoCodMapper.mapPageToDtoPage(promoCodService.retrieveAllPromoCods(page, size, sort));
    }

    @Override
    public ResponseEntity<?> getById(String name, String token) throws NotFoundEntityException {
        return new ResponseEntity<>(promoCodService.retrievePromoCodByName(name)
                .map(promoCodMapper::toDto)
                .orElseThrow(() -> new NotFoundEntityException("PromoCod")), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> createOrUpdate(PromoCodDTO promoCodDTO, BindingResult bindingResult, String token) {
        return isValidDate(promoCodDTO)
                ? save(promoCodDTO)
                : new ResponseEntity<>("The start and end dates of the PromoCod are not correct", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<?> delete(String name) {
        promoCodService.deleteById(name);
        return new ResponseEntity<>("PromoCod with this serial number was deleted", HttpStatus.ACCEPTED);
    }

    private boolean isValidDate(PromoCodDTO promoCodDTO) {
        LocalDate startDate = promoCodDTO.getStartDate();
        LocalDate expiredDate = promoCodDTO.getExpiredDate();

        return expiredDate == null || startDate.isBefore(expiredDate);
    }

    private ResponseEntity<?> save(PromoCodDTO promoCodDTO) {
        PromoCod promoCod = promoCodService.save(promoCodMapper.toEntity(promoCodDTO));
        return ResponseEntity.ok(promoCodMapper.toDto(promoCod));
    }
}
