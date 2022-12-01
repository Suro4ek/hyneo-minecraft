package eu.suro.api.user

import java.util.stream.Stream


class UserManager {
    /**
     * Получение всех пользователей
     * @return поток пользователей
     */
    fun getUsers(): Stream<IUser> {
        throw UnsupportedOperationException()
    }

    /**
     * Получение пользователя по игроку
     * @param player - игрок
     * @return пользователь
     */
    fun getUser(player: Any): IUser? {
        throw UnsupportedOperationException()
    }
}
