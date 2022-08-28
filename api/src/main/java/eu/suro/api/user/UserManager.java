package eu.suro.api.user;


import java.util.stream.Stream;

public interface UserManager<P> {
    default Stream<IUser<P>> getUsers() {
        throw new UnsupportedOperationException();
    }

    default IUser<P> getUser(P player) {
        throw new UnsupportedOperationException();
    }

}