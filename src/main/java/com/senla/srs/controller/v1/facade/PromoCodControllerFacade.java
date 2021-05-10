package com.senla.srs.controller.v1.facade;

import com.senla.srs.dto.promocod.PromoCodDTO;
import com.senla.srs.entity.PromoCod;
import com.senla.srs.exception.NotFoundEntityException;
import com.senla.srs.mapper.PromoCodMapper;
import com.senla.srs.security.JwtTokenData;
import com.senla.srs.service.PromoCodService;
import com.senla.srs.validator.PromoCodValidator;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import java.util.Optional;

@Controller
public class PromoCodControllerFacade extends AbstractFacade implements
        EntityControllerFacade<PromoCodDTO, PromoCodDTO, PromoCodDTO, String> {

    private final PromoCodService promoCodService;
    private final PromoCodMapper promoCodMapper;
    private final PromoCodValidator promoCodValidator;

    public PromoCodControllerFacade(PromoCodService promoCodService,
                                    PromoCodMapper promoCodMapper,
                                    PromoCodValidator promoCodValidator,
                                    JwtTokenData jwtTokenData) {
        super(jwtTokenData);
        this.promoCodService = promoCodService;
        this.promoCodMapper = promoCodMapper;
        this.promoCodValidator = promoCodValidator;
    }

    @Override
    public Page<PromoCodDTO> getAll(Integer page, Integer size, String sort, String token) {
        return promoCodMapper.mapPageToDtoPage(promoCodService.retrieveAllPromoCods(page, size, sort));
    }

    @Override
    public ResponseEntity<?> getById(String name, String token) throws NotFoundEntityException {
        return new ResponseEntity<>(promoCodService.retrievePromoCodByName(name)
                .map(promoCodMapper::toDto)
                .orElseThrow(() -> new NotFoundEntityException(PromoCod.class, name)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> createOrUpdate(PromoCodDTO promoCodDTO, BindingResult bindingResult, String token) {
        Optional<PromoCod> optionalPromoCod = promoCodService.retrievePromoCodByName(promoCodDTO.getName());

        var validatePromoCodDTO = promoCodValidator.validate(promoCodDTO, optionalPromoCod, bindingResult);

        return save(validatePromoCodDTO, bindingResult);
    }

    @Override
    public ResponseEntity<?> delete(String name) {
        promoCodService.deleteById(name);
        return new ResponseEntity<>("PromoCod with this serial number was deleted", HttpStatus.ACCEPTED);
    }

    @Override
    public String getExistEntityId(PromoCodDTO dto) {
        return dto.getName();
    }

    private ResponseEntity<?> save(PromoCodDTO promoCodDTO, BindingResult bindingResult) {

        if (!bindingResult.hasErrors()) {
            var promoCod = promoCodService.save(promoCodMapper.toEntity(promoCodDTO));

            return new ResponseEntity<>(promoCodMapper.toDto(promoCod), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

    }
}
