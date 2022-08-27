package eu.suro.metadata.type;

import eu.suro.api.user.bukkit.IUser;
import eu.suro.metadata.MetadataKey;
import eu.suro.metadata.MetadataMap;
import eu.suro.metadata.MetadataRegistry;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;

public interface UserBukkitMetadataRegistry extends MetadataRegistry<String> {

    /**
     * Produces a {@link MetadataMap} for the given player.
     *
     * @param player the player
     * @return a metadata map
     */
    @Nonnull
    MetadataMap provide(@Nonnull IUser player);

    /**
     * Gets a {@link MetadataMap} for the given player, if one already exists and has
     * been cached in this registry.
     *
     * @param player the player
     * @return a metadata map, if present
     */
    @Nonnull
    Optional<MetadataMap> get(@Nonnull IUser player);

    /**
     * Gets a map of the players with a given metadata key
     *
     * @param key the key
     * @param <K> the key type
     * @return an immutable map of players to key value
     */
    @Nonnull
    <K> Map<IUser, K> getAllWithKey(@Nonnull MetadataKey<K> key);

}