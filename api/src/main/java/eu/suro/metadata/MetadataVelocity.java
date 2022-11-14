package eu.suro.metadata;

import eu.suro.VelocityMain;
import eu.suro.api.user.IUser;
import eu.suro.metadata.listener.VelocityMetadataListener;
import eu.suro.metadata.type.UserMetadataRegistry;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public final class MetadataVelocity {

    private static final AtomicBoolean SETUP = new AtomicBoolean(false);

    // lazily load
    private static void ensureSetup() {
        if (SETUP.get()) {
            return;
        }

        if (!SETUP.getAndSet(true)) {
            VelocityMain.getInstance().
                    getProxyServer().
                    getEventManager().
                    register(VelocityMain.getInstance(), new VelocityMetadataListener());
            // cache housekeeping task
            VelocityMain.getInstance()
                    .getProxyServer()
                    .getScheduler()
                    .buildTask(VelocityMain.getInstance(),
                            () -> StandardMetadataRegistries.USER_METADATA_REGISTRY.cleanup())
                    .repeat(1L, TimeUnit.MINUTES)
                    .schedule();
        }
    }

    /**
     * Gets the {@link MetadataRegistry} for {@link eu.suro.api.user.bungee.IUser}s.
     *
     * @return the {@link UserBungeeMetadataRegistry}
     */
    public static UserMetadataRegistry users() {
        ensureSetup();
        return StandardMetadataRegistries.USER_METADATA_REGISTRY;
    }

    @Nonnull
    public static MetadataMap provideForUser(@Nonnull String name) {
        return users().provide(IUser.getUser(name.toLowerCase()));
    }

    @Nonnull
    public static MetadataMap provideForUser(@Nonnull IUser user) {
        return users().provide(user);
    }

    @Nonnull
    public static Optional<MetadataMap> getForUser(@Nonnull String name) {
        return users().get(IUser.getUser(name.toLowerCase()));
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
