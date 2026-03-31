package com.example.cogniproject.util;

import com.example.cogniproject.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Utility class for retrieving the currently authenticated user from the security context.
 */
public final class SecurityUtils {

    private SecurityUtils() {}

    /**
     * Returns the username of the currently authenticated principal.
     *
     * @throws UnauthorizedException if no authenticated principal is present
     */
    public static String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new UnauthorizedException("No authenticated user found in security context");
        }
        Object principal = auth.getPrincipal();
        if (principal instanceof UserDetails ud) {
            return ud.getUsername();
        }
        return principal.toString();
    }

    /**
     * Returns the role string of the currently authenticated user
     * (e.g. "ROLE_DOCTOR", "ROLE_ADMIN", "ROLE_CITIZEN").
     */
    public static String getCurrentUserRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getAuthorities().isEmpty()) {
            throw new UnauthorizedException("No authenticated user found in security context");
        }
        return auth.getAuthorities().iterator().next().getAuthority();
    }

    /** Convenience: returns true if the current user has the given role (without "ROLE_" prefix). */
    public static boolean hasRole(String role) {
        return getCurrentUserRole().equals("ROLE_" + role);
    }
}
