package eu.suro.api.user;


import java.util.stream.Stream;

public interface UserManager<P> {

    /**
        * Получение всех пользователей
        * @return поток пользователей
     */
    default Stream<IUser<P>> getUsers() {
        throw new UnsupportedOperationException();
    }

    /**
        * Получение пользователя по игроку
        * @param player - игрок
        * @return пользователь
     */
    default IUser<P> getUser(P player) {
        throw new UnsupportedOperationException();
    }

}