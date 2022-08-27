package eu.suro.api.user;

import eu.suro.api.user.bukkit.UserManager;

public class APIBukkit {
    private static UserManager userManager;

    private APIBukkit() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static UserManager getUserManager() {
        return userManager;
    }

    public static void setUserManager(UserManager userManager) {
        APIBukkit.userManager = userManager;
    }
}
