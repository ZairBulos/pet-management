package com.petmanagement.auth.application.port.out;

import com.petmanagement.auth.domain.model.aggregate.Authentication;

public interface AuthenticationRepositoryPort {
    void save(Authentication authentication);
    void replaceActiveAuthentication(Authentication authentication);
}
