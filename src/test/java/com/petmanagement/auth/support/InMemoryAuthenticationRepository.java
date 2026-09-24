package com.petmanagement.auth.support;

import com.petmanagement.auth.application.port.out.AuthenticationRepositoryPort;
import com.petmanagement.auth.domain.model.aggregate.Authentication;
import com.petmanagement.auth.domain.model.valueobject.AuthenticationId;
import com.petmanagement.auth.domain.model.valueobject.Email;

import java.time.Instant;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class InMemoryAuthenticationRepository implements AuthenticationRepositoryPort {

    private final Map<AuthenticationId, Authentication> authentications = new HashMap<>();

    @Override
    public void save(Authentication authentication) {
        authentications.put(authentication.getId(), authentication);
    }

    @Override
    public void replaceActiveAuthentication(Authentication authentication) {
        disableActiveAuthentication(authentication.getEmail(), Instant.now());
        save(authentication);
    }

    // === Helpers ===

    public void clear() {
        authentications.clear();
    }

    public int size() {
        return authentications.size();
    }

    public List<Authentication> findAllByEmail(Email email) {
        return authentications.values().stream()
                .filter(auth -> auth.getEmail().equals(email))
                .sorted(Comparator.comparing(Authentication::getCreatedAt))
                .toList();
    }

    public void saveAll(Authentication... authentications) {
        for (Authentication authentication : authentications) {
            save(authentication);
        }
    }

    private void disableActiveAuthentication(Email email, Instant now) {
        authentications.values().stream()
                .filter(auth -> auth.getEmail().equals(email) && !auth.isAuthenticated())
                .forEach(auth -> {
                    var disabled = Authentication.reconstitute(
                            auth.getId(),
                            auth.getEmail(),
                            auth.getHashedCode(),
                            auth.getExpiresAt(),
                            now,
                            auth.getCreatedAt()
                    );
                    authentications.put(auth.getId(), disabled);
                });
    }

}
