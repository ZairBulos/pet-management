package com.petmanagement.auth.infrastructure.config;

import com.petmanagement.shared.domain.model.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import tools.jackson.databind.json.JsonMapper;

import static org.junit.jupiter.api.Assertions.*;

class RestAuthenticationEntryPointTest {

    private JsonMapper jsonMapper;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private RestAuthenticationEntryPoint entryPoint;

    @BeforeEach
    void setUp() {
        jsonMapper = JsonMapper.builder().build();
        request = new MockHttpServletRequest("GET", "/api/test");
        response = new MockHttpServletResponse();

        entryPoint = new RestAuthenticationEntryPoint(jsonMapper);
    }

    @Test
    void shouldRespondUnauthorized() throws Exception {
        // When
        entryPoint.commence(request, response, new BadCredentialsException("invalid"));

        // Then
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
    }

    @Test
    void shouldRespondJsonContentType() throws Exception {
        // When
        entryPoint.commence(request, response, new BadCredentialsException("invalid"));

        // Then
        assertTrue(response.getContentType().startsWith(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void shouldWriteErrorBodyWithRequestPath() throws Exception {
        // When
        entryPoint.commence(request, response, new BadCredentialsException("invalid"));

        // Then
        var body = jsonMapper.readValue(response.getContentAsString(), ErrorResponse.class);
        assertEquals("UNAUTHORIZED", body.error());
        assertEquals("/api/test", body.path());
        assertNull(body.details());
    }

}
