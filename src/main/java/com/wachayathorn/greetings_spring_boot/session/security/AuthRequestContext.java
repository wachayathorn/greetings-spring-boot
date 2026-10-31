package com.wachayathorn.greetings_spring_boot.session.security;

public final class AuthRequestContext {

    private static final ThreadLocal<AuthenticatedUser> CURRENT = new ThreadLocal<>();

    private AuthRequestContext() {
    }

    public static void set(AuthenticatedUser user) {
        CURRENT.set(user);
    }

    public static AuthenticatedUser requireUser() {
        AuthenticatedUser user = CURRENT.get();
        if (user == null) {
            throw new IllegalStateException("No authenticated user on this request");
        }
        return user;
    }

    public static void clear() {
        CURRENT.remove();
    }
}
