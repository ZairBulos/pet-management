package com.petmanagement.auth.infrastructure.adapter.in.http;

import com.petmanagement.auth.application.port.in.RequestAuthenticationUseCase;
import com.petmanagement.auth.infrastructure.adapter.in.http.dto.request.RequestAuthenticationRequest;
import com.petmanagement.auth.infrastructure.adapter.in.http.mapper.AuthHttpMapper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AuthController.AUTH)
class AuthController {

    static final String AUTH = "/api/auth";
    static final String REQUEST = "/request";

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthHttpMapper mapper;
    private final RequestAuthenticationUseCase requestAuthenticationUseCase;

    AuthController(
            AuthHttpMapper mapper,
            RequestAuthenticationUseCase requestAuthenticationUseCase
    ) {
        this.mapper = mapper;
        this.requestAuthenticationUseCase = requestAuthenticationUseCase;
    }

    @PostMapping(REQUEST)
    ResponseEntity<Void> requestAuthentication(@Valid @RequestBody RequestAuthenticationRequest request) {
        log.info("Requesting authentication email={}", request.email());

        requestAuthenticationUseCase.execute(mapper.toRequestAuthenticationCommand(request));

        log.info("Authentication requested");

        return ResponseEntity.accepted().build();
    }

}
