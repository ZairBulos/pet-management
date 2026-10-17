package com.petmanagement.auth.support;

import com.petmanagement.auth.domain.model.enums.SessionRevocationReason;
import com.petmanagement.auth.domain.model.valueobject.*;

import java.time.Duration;

public final class TestSessionMother {

    // === Session IDs ===

    public static final SessionId DEFAULT_SESSION_ID =
            SessionId.of("24e27677-bd7c-43c6-828c-91f5c4fd931c");
    public static final SessionId ANOTHER_SESSION_ID =
            SessionId.of("ffed0f4b-843a-485e-8601-943b7a13319f");
    public static final SessionId THIRD_SESSION_ID =
            SessionId.of("ed842256-10e6-4de6-9783-c158c1598f52");
    public static final SessionId NON_EXISTENT__SESSION_ID =
            SessionId.of("aff29bc4-ddae-4c48-b7d2-7b97741eb338");

    // === Owner IDs ===

    public static final OwnerId EXISTING_OWNER_ID =
            OwnerId.of("5c7a797c-0e71-4f0e-bfff-68928343f104");
    public static final OwnerId NON_EXISTING_OWNER_ID =
            OwnerId.of("7ba15525-6b16-4b37-a4d8-35bf4920a3ae");
    public static final OwnerId ANOTHER_OWNER_ID =
            OwnerId.of("ba1d2526-f2c2-41ff-ab2c-521ef6ddb59d");

    // === Plain Refresh Tokens ===

    public static final SessionRefreshToken DEFAULT_PLAIN_REFRESH_TOKEN =
            new SessionRefreshToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiIsImtpZCI6IjhlMjEzZTdhYWMwMWIzMzkzM2MwNWU0NDNhOTk1ODFmIn0.e30.StjzbY0dVBxFnH6YtTnGTP4d2JxPFGcav2mRpA_uNT_xYAE3IsF7ELZgugmxxk5QHWCV9z3Mr5ivM0zBwOke6g");
    public static final SessionRefreshToken ANOTHER_PLAIN_REFRESH_TOKEN =
            new SessionRefreshToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiIsImtpZCI6IjhlMjEzZTdhYWMwMWIzMzkzM2MwNWU0NDNhOTk1ODFmIn0.e30._jHbI-Prb278TL_m2a61OKYwWGs88XmhyWunlOrTwd-v0Q_OuWz2irB5TYojHijkNYyIdOueKeTFMtAvmvD0jA");
    public static final SessionRefreshToken THIRD_PLAIN_REFRESH_TOKEN =
            new SessionRefreshToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiIsImtpZCI6IjE5OTJmY2MxMzVmNGZiNGEyZjhmYzNmM2Y3YjBlYjQ5In0.e30.UWsAvpCziVASG6KEMVmOBlt02TDdVCw6URRN85eKtAhJQecoaFeyA-puNAfqIhS9ctXC--cC0RYACMuuALzhWA");
    public static final SessionRefreshToken INVALID_PLAIN_REFRESH_TOKEN =
            new SessionRefreshToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJFUzI1NiIsImtpZCI6IjhlMjEzZTdhYWMwMWIzMzkzM2MwNWU0NDNhOTk1ODFmIn0.e30.PKb-m-qcYN5PTrbgmVBw7nIz19s_U9ISIoD_Wn8HiGZOkvtPM-cTFsgIRreKJvmI0hF85GcfwDUTRiYbE2O1Nw");

    // === Hashed Refresh Tokens ===

    public static final SessionHashedRefreshToken DEFAULT_HASHED_REFRESH_TOKEN =
            SessionHashedRefreshToken.from(DEFAULT_PLAIN_REFRESH_TOKEN);
    public static final SessionHashedRefreshToken ANOTHER_HASHED_REFRESH_TOKEN =
            SessionHashedRefreshToken.from(ANOTHER_PLAIN_REFRESH_TOKEN);
    public static final SessionHashedRefreshToken THIRD_HASHED_REFRESH_TOKEN =
            SessionHashedRefreshToken.from(THIRD_PLAIN_REFRESH_TOKEN);
    public static final SessionHashedRefreshToken INVALID_HASHED_REFRESH_TOKEN =
            SessionHashedRefreshToken.from(INVALID_PLAIN_REFRESH_TOKEN);

    // === Expirations ===

    public static final SessionExpiresAt DEFAULT_EXPIRES_AT =
            SessionExpiresAt.generate();
    public static final SessionExpiresAt EXPIRED_EXPIRES_AT =
            SessionExpiresAt.generate(Duration.ofMinutes(-1));
    public static final SessionExpiresAt FUTURE_EXPIRES_AT =
            SessionExpiresAt.generate(Duration.ofDays(14));

    // === Revocation Reasons ===

    public static final SessionRevocationReason ROTATED_REASON =
            SessionRevocationReason.ROTATED;
    public static final SessionRevocationReason LOGOUT_REASON =
            SessionRevocationReason.LOGOUT;
    public static final SessionRevocationReason REUSE_DETECTED_REASON =
            SessionRevocationReason.REUSE_DETECTED;

    private TestSessionMother() {
    }

}
