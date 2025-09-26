package fava.betaja.erp.service.impl;

import fava.betaja.erp.entities.common.OrganizationUnit;
import fava.betaja.erp.repository.security.UserRepository;
import fava.betaja.erp.enums.TokenType;
import fava.betaja.erp.payload.request.AuthenticationRequest;
import fava.betaja.erp.payload.request.RegisterRequest;
import fava.betaja.erp.payload.response.AuthenticationResponse;
import fava.betaja.erp.repository.common.OrganizationUnitRepository;
import fava.betaja.erp.service.AuthenticationService;
import fava.betaja.erp.service.JwtService;
import fava.betaja.erp.entities.security.Users;
import fava.betaja.erp.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final OrganizationUnitRepository organizationUnitRepository;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        OrganizationUnit organizationUnit = null;
        if (request.getOrganizationUnitId() != null) {
            Optional<OrganizationUnit> optionalOrganizationUnit = organizationUnitRepository
                    .findById(request.getOrganizationUnitId());
            if (optionalOrganizationUnit.isPresent()){
                organizationUnit = optionalOrganizationUnit.get();
            }
        }
        var user = Users.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .organizationUnit(organizationUnit)
                .active(true)
                .build();
        user = userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());


        return AuthenticationResponse.builder()
                .accessToken(jwt)
                .username(user.getUsername())
                .id(user.getId())
                .refreshToken(refreshToken.getToken())
                .permission(null)
                .tokenType(TokenType.BEARER.name())
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new IllegalArgumentException("Invalid username or password."));
        List<String> roles = user.getAuthorities()
                .stream()
                .map(s -> s.getAuthority())
                .toList();
        var jwt = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());
        return AuthenticationResponse.builder()
                .accessToken(jwt)
                .permission(roles)
                .username(user.getUsername())
                .id(user.getId())
                .refreshToken(refreshToken.getToken())
                .tokenType(TokenType.BEARER.name())
                .build();
    }
}
