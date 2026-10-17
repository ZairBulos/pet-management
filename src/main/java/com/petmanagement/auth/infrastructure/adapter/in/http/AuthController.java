package com.petmanagement.auth.infrastructure.adapter.in.http;

import com.petmanagement.auth.application.port.in.RefreshSessionUseCase;
import com.petmanagement.auth.application.port.in.RequestAuthenticationUseCase;
import com.petmanagement.auth.application.port.in.RevokeSessionUseCase;
import com.petmanagement.auth.application.port.in.VerifyAuthenticationUseCase;
import com.petmanagement.auth.infrastructure.adapter.in.http.dto.request.RefreshSessionRequest;
import com.petmanagement.auth.infrastructure.adapter.in.http.dto.request.RequestAuthenticationRequest;
import com.petmanagement.auth.infrastructure.adapter.in.http.dto.request.RevokeSessionRequest;
import com.petmanagement.auth.infrastructure.adapter.in.http.dto.request.VerifyAuthenticationRequest;
import com.petmanagement.auth.infrastructure.adapter.in.http.dto.response.TokenResponse;
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
    static final String VERIFY = "/verify";
    static final String REFRESH = "/refresh";
    static final String REVOKE = "/revoke";

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthHttpMapper mapper;
    private final RequestAuthenticationUseCase requestAuthenticationUseCase;
    private final VerifyAuthenticationUseCase verifyAuthenticationUseCase;
    private final RefreshSessionUseCase refreshSessionUseCase;
    private final RevokeSessionUseCase revokeSessionUseCase;

    AuthController(
            AuthHttpMapper mapper,
            RequestAuthenticationUseCase requestAuthenticationUseCase,
            VerifyAuthenticationUseCase verifyAuthenticationUseCase,
            RefreshSessionUseCase refreshSessionUseCase,
            RevokeSessionUseCase revokeSessionUseCase
    ) {
        this.mapper = mapper;
        this.requestAuthenticationUseCase = requestAuthenticationUseCase;
        this.verifyAuthenticationUseCase = verifyAuthenticationUseCase;
        this.refreshSessionUseCase = refreshSessionUseCase;
        this.revokeSessionUseCase = revokeSessionUseCase;
    }

    @PostMapping(REQUEST)
    ResponseEntity<Void> requestAuthentication(@Valid @RequestBody RequestAuthenticationRequest request) {
        log.info("Requesting authentication email={}", request.email());

        requestAuthenticationUseCase.execute(mapper.toRequestAuthenticationCommand(request));

        log.info("Authentication requested");

        return ResponseEntity.accepted().build();
    }

    @PostMapping(VERIFY)
    ResponseEntity<TokenResponse> verifyAuthentication(@Valid @RequestBody VerifyAuthenticationRequest request) {
        log.info("Verifying authentication email={}", request.email());

        var result = verifyAuthenticationUseCase.execute(mapper.toVerifyAuthenticationCommand(request));

        log.info("Authentication verified");

        return ResponseEntity.ok(mapper.toTokenResponse(result));
    }

    @PostMapping(REFRESH)
    ResponseEntity<TokenResponse> refreshSession(@Valid @RequestBody RefreshSessionRequest request) {
        log.info("Refreshing session");

        var result = refreshSessionUseCase.execute(mapper.toRefreshSessionCommand(request));

        log.info("Session refreshed");

        return ResponseEntity.ok(mapper.toTokenResponse(result));
    }

    @PostMapping(REVOKE)
    ResponseEntity<Void> revokeSession(@Valid @RequestBody RevokeSessionRequest request) {
        log.info("Revoking session");

        revokeSessionUseCase.execute(mapper.toRevokeSessionCommand(request));

        log.info("Session revoked");

        return ResponseEntity.noContent().build();
    }

}
