package com.petmanagement.auth.support;

import com.petmanagement.auth.domain.model.valueobject.*;

import java.time.Duration;
import java.util.function.BiPredicate;
import java.util.function.UnaryOperator;

public final class TestAuthenticationMother {

    // === Authentication IDs ===

    public static final AuthenticationId DEFAULT_AUTHENTICATION_ID =
            AuthenticationId.of("ae8e8c21-b5ca-455c-a677-42523d22c806");
    public static final AuthenticationId ANOTHER_AUTHENTICATION_ID =
            AuthenticationId.of("098db4f5-7219-4f4e-ae89-f07ef971cf19");
    public static final AuthenticationId NON_EXISTENT__AUTHENTICATION_ID =
            AuthenticationId.of("3a61ac2a-cb05-4268-9b59-c78b2f9e9c21");

    // === Emails ===

    public static final Email EMAIL_JOHN = new Email("john.doe@example.com");
    public static final Email EMAIL_JANE = new Email("jane.smith@test.com");
    public static final Email EMAIL_ROBERT = new Email("robert.j@workspace.org");
    public static final Email EMAIL_MARIA = new Email("maria.garcia@empresa.es");

    // === Hashing ====
    public static final UnaryOperator<String> DEFAULT_HASHER =
            plain -> "hashed-" + plain;

    public static BiPredicate<String, String> DEFAULT_VERIFIER =
            (plain, hash) -> ("hashed-" + plain).equals(hash);

    // === Plain Codes ===

    public static final AuthenticationCode DEFAULT_PLAIN_CODE =
            new AuthenticationCode("000943");

    public static final AuthenticationCode ANOTHER_PLAIN_CODE =
            new AuthenticationCode("683570");

    public static final AuthenticationCode INVALID_PLAIN_CODE =
            new AuthenticationCode("999999");

    // === Hashed Codes ===

    public static final AuthenticationHashedCode DEFAULT_HASHED_CODE
            = new AuthenticationHashedCode(DEFAULT_HASHER.apply(DEFAULT_PLAIN_CODE.value()));

    public static final AuthenticationHashedCode ANOTHER_HASHED_CODE =
            new AuthenticationHashedCode(DEFAULT_HASHER.apply(ANOTHER_PLAIN_CODE.value()));

    public static final AuthenticationHashedCode INVALID_HASHED_CODE =
            new AuthenticationHashedCode(DEFAULT_HASHER.apply(INVALID_PLAIN_CODE.value()));

    // === Expirations ===

    public static final AuthenticationExpiresAt DEFAULT_EXPIRES_AT =
            AuthenticationExpiresAt.generate();

    public static final AuthenticationExpiresAt EXPIRED_EXPIRES_AT =
            AuthenticationExpiresAt.generate(Duration.ofMinutes(-1));

    public static final AuthenticationExpiresAt FUTURE_EXPIRES_AT =
            AuthenticationExpiresAt.generate(Duration.ofMinutes(10));

    private TestAuthenticationMother() {
    }

}
