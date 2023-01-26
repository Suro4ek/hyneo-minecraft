package eu.suro.metadata

import eu.suro.VelocityMain.Companion.instance
import eu.suro.api.user.IUser
import eu.suro.api.user.IUser.Companion.getUser
import eu.suro.metadata.listener.MetadataListener
import eu.suro.metadata.type.UserMetadataRegistry
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
        if (!SETUP.getAndSet(true)) {
            instance!!.proxyServer.eventManager.register(instance, MetadataListener())
            // cache housekeeping task
            instance!!
                .proxyServer
                .scheduler
                .buildTask(
                    instance!!,
                    Runnable { StandardMetadataRegistries.USER_METADATA_REGISTRY.cleanup() })
                .repeat(1L, TimeUnit.MINUTES)
                .schedule()
        }
    }

    /**
     * Gets the [MetadataRegistry] for [eu.suro.api.user.bungee.IUser]s.
     *
     * @return the [UserBungeeMetadataRegistry]
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
        return users()[getUser(name.lowercase(Locale.getDefault()))!!]
    }

    fun getForUser(user: IUser): Optional<MetadataMap> {
        return users()[user]
    }

    fun <T> lookupUserWithKey(key: MetadataKey<T>): Map<IUser, T> {
        return users().getAllWithKey(key)
    }
}