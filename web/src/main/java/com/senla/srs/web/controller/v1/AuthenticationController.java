package com.senla.srs.web.controller.v1;

import com.senla.srs.core.dto.security.AuthenticationRequestDTO;
import com.senla.srs.core.dto.security.AuthenticationResponseDTO;
import com.senla.srs.core.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication REST Controller")
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(operationId = "authenticate", summary = "Authenticate", description = "Authenticate with JWT token")
    @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = AuthenticationResponseDTO.class)))
    @ApiResponse(responseCode = "400", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", content = @Content(mediaType = "application/json"))

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequestDTO request) {

        try {
            return ResponseEntity.ok(authenticationService.authenticate(request));
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid email/password combination", HttpStatus.FORBIDDEN);
        }

    }

}
