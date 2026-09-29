package com.petmanagement.auth.application.port.out;

import com.petmanagement.auth.domain.model.aggregate.Authentication;
import com.petmanagement.auth.domain.model.valueobject.Email;

import java.util.Optional;

public interface AuthenticationRepositoryPort {
    Optional<Authentication> findActiveByEmail(Email email);
    void save(Authentication authentication);
    void replaceActiveAuthentication(Authentication authentication);
}
