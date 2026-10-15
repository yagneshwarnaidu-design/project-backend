package com.hospital.management.service;

import com.hospital.management.dto.AuthRequest;
import com.hospital.management.dto.AuthResponse;
import com.hospital.management.dto.RegisterRequest;
import com.hospital.management.entity.Role;
import com.hospital.management.entity.User;
import com.hospital.management.enums.ERole;
import com.hospital.management.exception.ApiException;
import com.hospital.management.exception.ResourceNotFoundException;
import com.hospital.management.repository.RoleRepository;
import com.hospital.management.repository.UserRepository;
import com.hospital.management.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new ApiException("Username already exists");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new ApiException("Email already exists");
        }

        ERole roleName;
        try {
            roleName = ERole.valueOf(request.role().trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApiException("Invalid role. Allowed roles: ADMIN, DOCTOR, RECEPTIONIST, PATIENT");
        }

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPhone(request.phone());
        user.setEnabled(true);
        user.getRoles().add(role);

        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(savedUser);

        return buildAuthResponse(savedUser, token);
    }

    public AuthResponse login(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String token = jwtService.generateToken(authentication.getPrincipal() instanceof User principal ? principal : user);
        return buildAuthResponse(user, token);
    }

    private AuthResponse buildAuthResponse(User user, String token) {
        List<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .toList();

        return new AuthResponse(token, "Bearer", user.getUsername(), user.getEmail(), roles);
    }
}
