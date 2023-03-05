package eu.suro.metadata

import eu.suro.SpigotMain.Companion.instance
import eu.suro.api.user.IUser
import eu.suro.api.user.IUser.Companion.getUser
import eu.suro.metadata.listener.MetadataListener
import eu.suro.metadata.type.UserMetadataRegistry
import eu.suro.utils.ScheduleBukkit.Companion.timerAsync
import org.bukkit.Bukkit
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

object Metadata {
    private val SETUP = AtomicBoolean(false)

    // lazily load
    fun ensureSetup() {
        if (SETUP.get()) {
            return
        }
        Bukkit.getPluginManager().registerEvents(MetadataListener(), instance)
        if (!SETUP.getAndSet(true)) {
            timerAsync({ StandardMetadataRegistries.USER_METADATA_REGISTRY.cleanup() }, 1, 1, TimeUnit.MINUTES)
        }
    }

    /**
     * Gets the [MetadataRegistry] for [eu.suro.api.user.IUser]s.
     *
     * @return the [UserMetadataRegistry]
     */
    fun users(): UserMetadataRegistry {
        ensureSetup()
        return StandardMetadataRegistries.USER_METADATA_REGISTRY
    }

    fun provideForUser(name: String): MetadataMap {
        return users().provide(getUser(name.lowercase(Locale.getDefault()))!!)
    }

    fun provideForUser(user: IUser): MetadataMap {
        return users().provide(user)
    }

    fun getForUser(name: String): Optional<MetadataMap> {
        return users()[getUser(name)!!]
    }

    fun getForUser(user: IUser): Optional<MetadataMap> {
        return users()[user]
    }

    fun <T> lookupUserWithKey(key: MetadataKey<T>): Map<IUser, T> {
        return users().getAllWithKey(key)
    }
}