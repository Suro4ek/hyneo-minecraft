package eu.suro.api.user;

import eu.suro.api.user.bungee.UserManager;

public class APIBungee {
    private static UserManager userManager;

    private APIBungee() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static UserManager getUserManager() {
        return userManager;
    }

    public static void setUserManager(UserManager userManager) {
        APIBungee.userManager = userManager;
    }
}