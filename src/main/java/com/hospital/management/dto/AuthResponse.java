package com.hospital.management.dto;

import java.util.List;

public record AuthResponse(
        String token,
        String type,
        String username,
        String email,
        List<String> roles
) {
}
