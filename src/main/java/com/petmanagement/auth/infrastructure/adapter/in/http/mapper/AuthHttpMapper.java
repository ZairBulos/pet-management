package com.petmanagement.auth.infrastructure.adapter.in.http.mapper;

import com.petmanagement.auth.application.port.in.RequestAuthenticationUseCase;
import com.petmanagement.auth.domain.model.valueobject.Email;
import com.petmanagement.auth.infrastructure.adapter.in.http.dto.request.RequestAuthenticationRequest;
import org.springframework.stereotype.Component;

@Component
public class AuthHttpMapper {

    public RequestAuthenticationUseCase.RequestAuthenticationCommand toRequestAuthenticationCommand(
            RequestAuthenticationRequest request
    ) {
        return new RequestAuthenticationUseCase.RequestAuthenticationCommand(new Email(request.email()));
    }

}
