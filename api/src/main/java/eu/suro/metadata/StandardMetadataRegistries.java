package eu.suro.metadata;

import com.google.common.collect.ImmutableMap;
import eu.suro.api.user.IUser;
import eu.suro.metadata.type.UserMetadataRegistry;

import java.util.*;

import javax.annotation.Nonnull;

/**
 * The Metadata registries provided by helper.
 *
 * These instances can be accessed through {@link Metadata}.
 */
public final class StandardMetadataRegistries {

    public static final UserMetadataRegistry USER_METADATA_REGISTRY = new UserMetadataRegistryImpl();
//    public static final EntityMetadataRegistry ENTITY = new EntityRegistry();
//    public static final BlockMetadataRegistry BLOCK = new BlockRegistry();
//    public static final WorldMetadataRegistry WORLD = new WorldRegistry();

    private static final class UserMetadataRegistryImpl extends AbstractMetadataRegistry<IUser> implements UserMetadataRegistry {

        @Nonnull
        @Override
        public MetadataMap provide(@Nonnull IUser player) {
            Objects.requireNonNull(player, "player");
            return provide(IUser.getUser(player.getName().toLowerCase(Locale.ROOT)));
        }

        @Nonnull
        @Override
        public Optional<MetadataMap> get(@Nonnull IUser player) {
            Objects.requireNonNull(player, "player");
            return get(IUser.getUser(player.getName().toLowerCase(Locale.ROOT)));
        }

        @Nonnull
        @Override
        public <K> Map<IUser, K> getAllWithKey(@Nonnull MetadataKey<K> key) {
            Objects.requireNonNull(key, "key");
            ImmutableMap.Builder<IUser, K> ret = ImmutableMap.builder();
            this.cache.asMap().forEach((name, map) -> map.get(key).ifPresent(t -> {
                IUser player = IUser.getUser(name);
                if (player != null) {
                    ret.put(player, t);
                }
            }));
            return ret.build();
        }
    }

//
//    private static final class EntityRegistry extends AbstractMetadataRegistry<UUID> implements EntityMetadataRegistry {
//
//        @Nonnull
//        @Override
//        public MetadataMap provide(@Nonnull Entity entity) {
//            Objects.requireNonNull(entity, "entity");
//            return provide(entity.getUniqueId());
//        }
//
//        @Nonnull
//        @Override
//        public Optional<MetadataMap> get(@Nonnull Entity entity) {
//            Objects.requireNonNull(entity, "entity");
//            return get(entity.getUniqueId());
//        }
//
//        @Nonnull
//        @Override
//        public <K> Map<Entity, K> getAllWithKey(@Nonnull MetadataKey<K> key) {
//            Objects.requireNonNull(key, "key");
//            ImmutableMap.Builder<Entity, K> ret = ImmutableMap.builder();
//            this.cache.asMap().forEach((uuid, map) -> map.get(key).ifPresent(t -> {
//                Entity entity = Bukkit.getEntity(uuid);
//                if (entity != null) {
//                    ret.put(entity, t);
//                }
//            }));
//            return ret.build();
//        }
//    }
//
//    private static final class BlockRegistry extends AbstractMetadataRegistry<BlockPosition> implements BlockMetadataRegistry {
//
//        @Nonnull
//        @Override
//        public MetadataMap provide(@Nonnull Block block) {
//            Objects.requireNonNull(block, "block");
//            return provide(BlockPosition.of(block));
//        }
//
//        @Nonnull
//        @Override
//        public Optional<MetadataMap> get(@Nonnull Block block) {
//            Objects.requireNonNull(block, "block");
//            return get(BlockPosition.of(block));
//        }
//
//        @Nonnull
//        @Override
//        public <K> Map<BlockPosition, K> getAllWithKey(@Nonnull MetadataKey<K> key) {
//            Objects.requireNonNull(key, "key");
//            ImmutableMap.Builder<BlockPosition, K> ret = ImmutableMap.builder();
//            this.cache.asMap().forEach((pos, map) -> map.get(key).ifPresent(t -> ret.put(pos, t)));
//            return ret.build();
//        }
//    }
//
//    private static final class WorldRegistry extends AbstractMetadataRegistry<UUID> implements WorldMetadataRegistry {
//
//        @Nonnull
//        @Override
//        public MetadataMap provide(@Nonnull World world) {
//            Objects.requireNonNull(world, "world");
//            return provide(world.getUID());
//        }
//
//        @Nonnull
//        @Override
//        public Optional<MetadataMap> get(@Nonnull World world) {
//            Objects.requireNonNull(world, "world");
//            return get(world.getUID());
//        }
//
//        @Nonnull
//        @Override
//        public <K> Map<World, K> getAllWithKey(@Nonnull MetadataKey<K> key) {
//            Objects.requireNonNull(key, "key");
//            ImmutableMap.Builder<World, K> ret = ImmutableMap.builder();
//            this.cache.asMap().forEach((uuid, map) -> map.get(key).ifPresent(t -> {
//                World world = Bukkit.getWorld(uuid);
//                if (world != null) {
//                    ret.put(world, t);
//                }
//            }));
//            return ret.build();
//        }
//    }

    private StandardMetadataRegistries() {
        throw new UnsupportedOperationException("This class cannot be instantiated");
    }

}