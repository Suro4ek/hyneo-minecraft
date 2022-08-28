package eu.suro.api.user;


public class APIUser{

    private static UserManager userManager;

    private APIUser() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static UserManager getUserManager() {
        return userManager;
    }

    public static void setUserManager(UserManager userManager) {
        APIUser.userManager = userManager;
    }
}
