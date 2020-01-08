package com.marvel.gps.security.jwt;

import com.marvel.gps.util.LoggingUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    private static final LoggingUtil LOG = new LoggingUtil(JwtAuthEntryPoint.class);

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        final String authException = (String) httpServletRequest.getAttribute("authException");
        LOG.error("Unauthorized error. Message - Unauthorized", "");
        if(e instanceof InternalAuthenticationServiceException){
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization Failure.  Username or password credential mismatch.");
        }

            if(authException != null)
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization Failure.  "+ authException);
        else
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization Failure.  "+ e.getMessage());

    }


}
