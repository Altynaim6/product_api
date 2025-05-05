package com.example.product_api.security.oauth2;

import com.example.product_api.model.domain.RefreshToken;
import com.example.product_api.model.domain.User;
import com.example.product_api.model.enums.Role;
import com.example.product_api.repository.UserRepository;
import com.example.product_api.security.jwt.JwtTokenProvider;
import com.example.product_api.service.RefreshTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        if (email == null || email.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "❌ OAuth2 error: email not found. Ensure scope=email is present.");
            return;
        }

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name != null && !name.isBlank() ? name : "NoName");
            newUser.setPassword(""); // No password for OAuth2
            newUser.setRole(Role.CUSTOMER);
            newUser.setEnabled(true); // trusted via OAuth2
            return userRepository.save(newUser);
        });

        String accessToken = jwtTokenProvider.generateAccessToken(
                user.getEmail(),
                user.getName(),
                user.getRole().name()
        );
        RefreshToken refreshToken = refreshTokenService.getOrCreateRefreshToken(user);

        String redirectUrl = String.format("/home.html?accessToken=%s&refreshToken=%s",
                URLEncoder.encode(accessToken, StandardCharsets.UTF_8),
                URLEncoder.encode(refreshToken.getToken(), StandardCharsets.UTF_8)
        );

        response.sendRedirect(redirectUrl);

    }

}