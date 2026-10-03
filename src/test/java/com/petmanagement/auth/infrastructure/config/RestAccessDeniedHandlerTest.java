package com.petmanagement.auth.infrastructure.config;

import com.petmanagement.shared.domain.model.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import tools.jackson.databind.json.JsonMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RestAccessDeniedHandlerTest {

    private JsonMapper jsonMapper;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private RestAccessDeniedHandler handler;

    @BeforeEach
    void setup() {
        jsonMapper = JsonMapper.builder().build();
        request = new MockHttpServletRequest("GET", "/api/test");
        response = new MockHttpServletResponse();

        handler = new RestAccessDeniedHandler(jsonMapper);
    }

    @Test
    void shouldRespondForbidden() throws Exception {
        // When
        handler.handle(request, response, new AccessDeniedException("denied"));

        // Then
        assertEquals(HttpServletResponse.SC_FORBIDDEN, response.getStatus());
    }

    @Test
    void shouldRespondJsonContentType() throws Exception {
        // When
        handler.handle(request, response, new AccessDeniedException("denied"));

        // Then
        assertTrue(response.getContentType().startsWith(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    void shouldWriteErrorBodyWithRequestPath() throws Exception {
        // When
        handler.handle(request, response, new AccessDeniedException("denied"));

        // Then
        var body = jsonMapper.readValue(response.getContentAsString(), ErrorResponse.class);
        assertEquals("FORBIDDEN", body.error());
        assertEquals("/api/test", body.path());
    }

}
