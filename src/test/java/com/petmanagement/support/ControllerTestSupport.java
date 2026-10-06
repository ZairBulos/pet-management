package com.petmanagement.support;

import com.petmanagement.auth.application.port.out.TokenProviderPort;
import com.petmanagement.auth.infrastructure.adapter.in.security.JwtAuthenticationFilter;
import com.petmanagement.auth.infrastructure.config.RestAccessDeniedHandler;
import com.petmanagement.auth.infrastructure.config.RestAuthenticationEntryPoint;
import com.petmanagement.auth.infrastructure.config.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

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

}
