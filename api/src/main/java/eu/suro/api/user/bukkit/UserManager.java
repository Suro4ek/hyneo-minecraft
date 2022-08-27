package eu.suro.api.user.bukkit;

import org.bukkit.entity.Player;

import java.util.stream.Stream;

public interface UserManager {
    default Stream<IUser> getUsers() {
        throw new UnsupportedOperationException();
    }

    default IUser getUser(Player player) {
        throw new UnsupportedOperationException();
    }

}