package eu.suro.api.user.bungee;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.stream.Stream;

public interface UserManager {
    default Stream<IUser> getUsers() {
        throw new UnsupportedOperationException();
    }

    default IUser getUser(ProxiedPlayer player) {
        throw new UnsupportedOperationException();
    }

}