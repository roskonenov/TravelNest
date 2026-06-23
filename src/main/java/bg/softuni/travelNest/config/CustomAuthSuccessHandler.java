package bg.softuni.travelNest.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final RequestCache requestCache = new HttpSessionRequestCache();
    
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        String redirect = request.getParameter("redirect");

        if (redirect != null && !redirect.isBlank()) {
            response.sendRedirect(redirect);
            return;
        }

        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (savedRequest != null) {
            response.sendRedirect(savedRequest.getRedirectUrl());
            return;
        }

        response.sendRedirect("/");
    }
}
