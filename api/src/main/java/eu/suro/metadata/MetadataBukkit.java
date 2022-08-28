package eu.suro.metadata;

import eu.suro.SpigotMain;
import eu.suro.api.user.IUser;
import eu.suro.messanger.listener.BukkitMessageListener;
import eu.suro.metadata.type.UserMetadataRegistry;
import eu.suro.utils.ScheduleBukkit;
import org.bukkit.Bukkit;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public final class MetadataBukkit {

    private static final AtomicBoolean SETUP = new AtomicBoolean(false);

    // lazily load
    private static void ensureSetup() {
        if (SETUP.get()) {
            return;
        }
        Bukkit.getPluginManager().registerEvents(new BukkitMessageListener(), SpigotMain.getInstance());
        if (!SETUP.getAndSet(true)) {
            ScheduleBukkit.timerAsync(() -> {
                StandardMetadataRegistries.USER_METADATA_REGISTRY.cleanup();
            }, 1, 1, TimeUnit.MINUTES);
        }
    }

    /**
     * Gets the {@link MetadataRegistry} for {@link eu.suro.api.user.bukkit.IUser}s.
     *
     * @return the {@link UserBukkitMetadataRegistry}
     */
    public static UserMetadataRegistry users() {
        ensureSetup();
        return StandardMetadataRegistries.USER_METADATA_REGISTRY;
    }

    @Nonnull
    public static MetadataMap provideForUser(@Nonnull String name) {
        return users().provide(name);
    }

    @Nonnull
    public static MetadataMap provideForUser(@Nonnull IUser user) {
        return users().provide(user);
    }

    @Nonnull
    public static Optional<MetadataMap> getForUser(@Nonnull String name) {
        return users().get(name);
    }

    @Nonnull
    public static Optional<MetadataMap> getForUser(@Nonnull IUser user) {
        return users().get(user);
    }

    @Nonnull
    public static <T> Map<IUser, T> lookupUserWithKey(@Nonnull MetadataKey<T> key) {
        return users().getAllWithKey(key);
    }

}
