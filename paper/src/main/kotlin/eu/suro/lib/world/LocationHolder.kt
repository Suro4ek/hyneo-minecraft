package eu.suro.lib.world

import eu.suro.utils.math.Vector3D

import eu.suro.utils.math.Vector3Df


class LocationHolder : Wrapper, Serializable {
    /**
     * The X coordinate of this location.
     */
    private val x = 0.0

    /**
     * The Y coordinate of this location.
     */
    private val y = 0.0

    /**
     * The Z coordinate of this location.
     */
    private val z = 0.0

    /**
     * The yaw of this location (horizontal rotation), 0 is the default.
     */
    private val yaw = 0f

    /**
     * The pitch of this location (vertical rotation), 0 is the default.
     */
    private val pitch = 0f

    /**
     * The world of this location.
     */
    private val world: WorldHolder? = null

    /**
     * Clones the current location and increments the coordinates by the supplied values.
     *
     * @param x X coordinate to add
     * @param y Y coordinate to add
     * @param z Z coordinate to add
     * @return the new location
     */
    @Contract(value = "_,_,_ -> new", pure = true)
    @NotNull
    fun add(x: Double, y: Double, z: Double): LocationHolder {
        return toBuilder()
            .x(this.x + x)
            .y(this.y + y)
            .z(this.z + z)
            .build()
    }

    /**
     * Clones the current location and increments the coordinates by the XYZ values of the supplied [LocationHolder].
     *
     * @param holder the location holder to add
     * @return the new location
     */
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    fun add(@NotNull holder: LocationHolder): LocationHolder {
        return add(holder.getX(), holder.getY(), holder.getZ())
    }

    /**
     * Clones the current location and increments the coordinates by the XYZ values of the supplied [Vector3D].
     *
     * @param vec the vector to add
     * @return the new location
     */
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    fun add(@NotNull vec: Vector3D): LocationHolder {
        return add(vec.x, vec.y, vec.z)
    }

    /**
     * Clones the current location and increments the coordinates by the XYZ values of the supplied [Vector3Df].
     *
     * @param vec the vector to add
     * @return the new location
     */
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    fun add(@NotNull vec: Vector3Df): LocationHolder {
        return add(vec.x.toDouble(), vec.y.toDouble(), vec.z.toDouble())
    }

    /**
     * Clones the current location and increments the coordinates by the XYZ values of the supplied [BlockFace.getBlockDirection].
     *
     * @param blockFace the block face to add
     * @return the new location
     */
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    fun add(@NotNull blockFace: BlockFace): LocationHolder {
        return add(blockFace.getBlockDirection())
    }

    /**
     * Clones the current location and increments the coordinates by the XYZ values of the supplied [BlockFace.getBlockDirection].
     *
     * @param blockFace the block face to add
     * @param distance  how far in the direction the new location should be
     * @return the new location
     */
    @Contract(value = "_,_ -> new", pure = true)
    @NotNull
    fun add(@NotNull blockFace: BlockFace, distance: Int): LocationHolder {
        return add(blockFace.getBlockDirection().multiply(distance))
    }

    /**
     * Clones the current location and decrements the coordinates by the XYZ values of the supplied [Vector3Df].
     *
     * @param x X coordinate to subtract
     * @param y Y coordinate to subtract
     * @param z Z coordinate to subtract
     * @return the new location
     */
    @Contract(value = "_,_,_ -> new", pure = true)
    @NotNull
    fun subtract(x: Double, y: Double, z: Double): LocationHolder {
        return toBuilder()
            .x(this.x - x)
            .y(this.y - y)
            .z(this.z - z)
            .build()
    }

    /**
     * Clones the current location and decrements the coordinates by the XYZ values of the supplied [LocationHolder].
     *
     * @param holder the location holder to subtract
     * @return the new location
     */
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    fun subtract(@NotNull holder: LocationHolder): LocationHolder {
        return subtract(holder.getX(), holder.getY(), holder.getZ())
    }

    /**
     * Clones the current location and decrements the coordinates by the XYZ values of the supplied [Vector3D].
     *
     * @param vec the vector to subtract
     * @return the new location
     */
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    fun subtract(@NotNull vec: Vector3D): LocationHolder {
        return subtract(vec.x, vec.y, vec.z)
    }

    /**
     * Clones the current location and decrements the coordinates by the XYZ values of the supplied [Vector3Df].
     *
     * @param vec the vector to subtract
     * @return the new location
     */
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    fun subtract(@NotNull vec: Vector3Df): LocationHolder {
        return subtract(vec.x.toDouble(), vec.y.toDouble(), vec.z.toDouble())
    }

    /**
     * Clones the current location and decrements the coordinates by the XYZ values of the supplied [BlockFace.getBlockDirection].
     *
     * @param blockFace the block face to add
     * @return the new location
     */
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    fun subtract(@NotNull blockFace: BlockFace): LocationHolder {
        return subtract(blockFace.getBlockDirection())
    }

    /**
     * Clones the current location and decrements the coordinates by the XYZ values of the supplied [BlockFace.getBlockDirection].
     *
     * @param blockFace the block face to add
     * @param distance  how far in the direction the new location should be
     * @return the new location
     */
    @Contract(value = "_,_ -> new", pure = true)
    @NotNull
    fun subtract(@NotNull blockFace: BlockFace, distance: Int): LocationHolder {
        return subtract(blockFace.getBlockDirection().multiply(distance))
    }

    /**
     * {@inheritDoc}
     */
    fun <T> `as`(type: Class<T>?): T {
        return LocationMapper.convert(this, type)
    }

    /**
     * Gets the squared distance between this location and a supplied one.
     *
     * @param holder the second location holder
     * @return the squared distance
     */
    fun getDistanceSquared(@NotNull holder: LocationHolder): Double {
        return MathUtils.square(getX() - holder.getX()) +
                MathUtils.square(getY() - holder.getY()) +
                MathUtils.square(getZ() - holder.getZ())
    }

    /**
     * Converts this location to a [Vector3D].
     *
     * @return the vector
     */
    @NotNull
    fun asVector(): Vector3D {
        return Vector3D(x, y, z)
    }

    /**
     * Converts this location to a [Vector3Df].
     *
     * @return the vector
     */
    @NotNull
    fun asVectorf(): Vector3Df {
        return Vector3Df(x.toFloat(), y.toFloat(), z.toFloat())
    }

    val blockX: Int
        /**
         * Gets the rounded X coordinate of this location (a block coordinate).
         *
         * @return the block X coordinate
         */
        get() = Math.floor(x).toInt()
    val blockY: Int
        /**
         * Gets the rounded Y coordinate of this location (a block coordinate).
         *
         * @return the block Y coordinate
         */
        get() = Math.floor(y).toInt()
    val blockZ: Int
        /**
         * Gets the rounded Z coordinate of this location (a block coordinate).
         *
         * @return the block Z coordinate
         */
        get() = Math.floor(z).toInt()

    @get:NotNull
    val facingDirection: Vector3D
        /**
         * Gets the facing direction vector of this location.
         *
         * @return the facing direction vector
         */
        get() {
            val vector = Vector3D()
            vector.y = -Math.sin(Math.toRadians(pitch.toDouble()))
            val xz = Math.cos(Math.toRadians(pitch.toDouble()))
            vector.x = -xz * Math.sin(Math.toRadians(yaw.toDouble()))
            vector.z = xz * Math.cos(Math.toRadians(yaw.toDouble()))
            return vector
        }

    @get:NotNull
    val block: BlockHolder
        /**
         * Gets the block at this location.
         *
         * @return the block
         */
        get() = `as`(BlockHolder::class.java)

    @get:NotNull
    val chunk: ChunkHolder
        /**
         * Gets the chunk which this location is in.
         *
         * @return the chunk
         */
        get() = getWorld().getChunkAt(this).orElseThrow()

    /**
     * Spawns a particle at this location.
     *
     * @param particle the particle
     */
    fun sendParticle(@NotNull particle: ParticleHolder?) {
        getWorld().sendParticle(particle, this)
    }

    @get:NotNull
    val highestBlock: BlockHolder
        /**
         * Gets the highest non-empty block on the X and Z coordinates of this location.
         *
         * @return the highest non-empty block
         */
        get() = getWorld().getHighestBlockAt(blockX, blockZ)
    val highestY: Int
        /**
         * Gets the highest non-empty Y coordinate on the X and Z coordinates of this location.
         *
         * @return the highest non-empty Y coordinate
         */
        get() = getWorld().getHighestYAt(blockX, blockZ)

    /**
     * Clones this location holder.
     *
     * @return the cloned holder
     */
    @NotNull
    override fun clone(): LocationHolder {
        return LocationHolder(
            x,
            y,
            z,
            yaw,
            pitch,
            world
        )
    }

    /**
     * Gets the entities in the radius from this location.
     *
     * @param radius the radius
     * @return the entities
     */
    @NotNull
    fun getNearbyEntities(radius: Int): List<EntityBasic> {
        return world.getEntities().stream()
            .filter { e -> isInRange(e.getLocation(), radius) }
            .collect(Collectors.toList())
    }

    /**
     * Gets the entities extending the supplied class in the radius from this location.
     *
     * @param clazz  the entity type class
     * @param radius the radius
     * @param <T>    the entity type
     * @return the entities
    </T> */
    fun <T : EntityBasic?> getNearbyEntitiesByClass(@NotNull clazz: Class<T>?, radius: Int): @NotNull MutableList<T>? {
        return world.getEntitiesByClass(clazz).stream()
            .filter { e -> isInRange(e.getLocation(), radius) }
            .collect(Collectors.toList())
    }

    /**
     * Compares the supplied world to the world of this location.
     *
     * @param holder the world to compare
     * @return is the world the same?
     */
    fun isWorldSame(@NotNull holder: LocationHolder): Boolean {
        return getWorld().equals(holder.getWorld())
    }

    /**
     * Checks if the supplied location is within the radius from this location.
     *
     * @param holder   the location holder to compare
     * @param distance the radius
     * @return is the supplied location within the radius of this location?
     */
    fun isInRange(@NotNull holder: LocationHolder, distance: Int): Boolean {
        return getDistanceSquared(holder) < distance
    }

    /**
     * Checks if the supplied location is out of range from this location.
     *
     * @param holder   the location holder to compare
     * @param distance the radius
     * @return is the supplied location out of range of this location?
     */
    fun outOfRange(@NotNull holder: LocationHolder, distance: Int): Boolean {
        return getDistanceSquared(holder) >= distance
    }

    /**
     * Sets the [yaw][.getYaw] and [pitch][.getPitch] to point
     * in the direction of the vector.
     *
     * @param vector the direction vector
     * @return the new location
     */
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    fun setDirection(@NotNull vector: Vector3D): LocationHolder {
        /*
         * Sin = Opp / Hyp
         * Cos = Adj / Hyp
         * Tan = Opp / Adj
         *
         * x = -Opp
         * z = Adj
         */
        val _2PI = 2 * Math.PI
        val x = vector.x
        val z = vector.z
        if (x == 0.0 && z == 0.0) {
            val pitch = (if (vector.y > 0) -90 else 90).toFloat()
            return LocationHolder(this.x, y, this.z, yaw, pitch, world)
        }
        val theta = Math.atan2(-x, z)
        val yaw = Math.toDegrees((theta + _2PI) % _2PI).toFloat()
        val x2 = Math.pow(x, 2.0)
        val z2 = Math.pow(z, 2.0)
        val xz = Math.sqrt(x2 + z2)
        val pitch = Math.toDegrees(Math.atan(-vector.y / xz)).toFloat()
        return LocationHolder(this.x, y, this.z, yaw, pitch, world)
    }
}