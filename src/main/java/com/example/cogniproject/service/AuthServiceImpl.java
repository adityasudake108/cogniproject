package com.example.cogniproject.service;

import com.example.cogniproject.dto.LoginRequest;
import com.example.cogniproject.dto.LoginResponse;
import com.example.cogniproject.entity.User;
import com.example.cogniproject.repository.UserRepository;
import com.example.cogniproject.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * Implementation of AuthService that delegates to Spring Security's AuthenticationManager.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JwtTokenProvider jwtTokenProvider,
                           UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        // Authenticate against stored credentials; throws BadCredentialsException if invalid
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        String token = jwtTokenProvider.generateToken(authentication);

        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow();

        return new LoginResponse(
                token,
                user.getUsername(),
                user.getRole().name(),
                jwtTokenProvider.getJwtExpiration()
        );
    }
}
