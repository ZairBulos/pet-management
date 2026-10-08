package com.petmanagement.support;

import com.petmanagement.auth.application.port.out.TokenProviderPort;
import com.petmanagement.auth.infrastructure.adapter.in.security.JwtAuthenticationFilter;
import com.petmanagement.auth.infrastructure.config.RestAccessDeniedHandler;
import com.petmanagement.auth.infrastructure.config.RestAuthenticationEntryPoint;
import com.petmanagement.auth.infrastructure.config.SecurityConfig;
import com.petmanagement.owners.support.TestOwnerMother;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import tools.jackson.databind.json.JsonMapper;

import java.util.Collections;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@Import({
        SecurityConfig.class,
        JwtAuthenticationFilter.class,
        RestAuthenticationEntryPoint.class,
        RestAccessDeniedHandler.class,
})
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected JsonMapper jsonMapper;

    @MockitoBean
    protected TokenProviderPort tokenProvider;

    // === Anonymous ===

    protected ResultActions performGet(String url) throws Exception {
        return mockMvc.perform(get(url));
    }

    protected ResultActions performPost(String url, Object body) throws Exception {
        return mockMvc.perform(withJson(post(url), body));
    }

    protected ResultActions performPut(String url, Object body) throws Exception {
        return mockMvc.perform(withJson(put(url), body));
    }

    // === Authenticated ===

    protected ResultActions performAuthenticatedGet(String url) throws Exception {
        return mockMvc.perform(get(url).with(authenticated()));
    }

    protected ResultActions performAuthenticatedPost(String url, Object body) throws Exception {
        return mockMvc.perform(withJson(post(url), body).with(authenticated()));
    }

    protected ResultActions performAuthenticatedPut(String url, Object body) throws Exception {
        return mockMvc.perform(withJson(put(url), body).with(authenticated()));
    }

    // === Internals ===

    private RequestPostProcessor authenticated() {
        return authentication(
                new UsernamePasswordAuthenticationToken(
                        TestOwnerMother.DEFAULT_OWNER_ID.value(), null, Collections.emptyList()
                )
        );
    }

    private MockHttpServletRequestBuilder withJson(MockHttpServletRequestBuilder request, Object body) {
        return request
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(body));
    }

}
