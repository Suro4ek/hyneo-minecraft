package eu.suro.metadata;

import eu.suro.BungeeMain;
import eu.suro.api.user.bungee.IUser;
import eu.suro.metadata.listener.BungeeMetadataListener;
import eu.suro.metadata.type.UserBungeeMetadataRegistry;
import eu.suro.utils.ScheduleBungee;
import net.md_5.bungee.api.ProxyServer;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public final class MetadataBungee {

    private static final AtomicBoolean SETUP = new AtomicBoolean(false);

    // lazily load
    private static void ensureSetup() {
        if (SETUP.get()) {
            return;
        }

        if (!SETUP.getAndSet(true)) {
            ProxyServer.getInstance().getPluginManager().registerListener(BungeeMain.getInstance(), new BungeeMetadataListener());
            // cache housekeeping task
            ScheduleBungee.timer(() -> {
                StandardMetadataRegistries.USER_BUNGEE_METADATA_REGISTRY.cleanup();
           }, 1, 1, TimeUnit.MINUTES);
        }
    }

    /**
     * Gets the {@link MetadataRegistry} for {@link eu.suro.api.user.bungee.IUser}s.
     *
     * @return the {@link UserBungeeMetadataRegistry}
     */
    public static UserBungeeMetadataRegistry users() {
        ensureSetup();
        return StandardMetadataRegistries.USER_BUNGEE_METADATA_REGISTRY;
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
