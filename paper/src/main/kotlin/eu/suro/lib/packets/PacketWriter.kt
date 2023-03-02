package eu.suro.lib.packets

import eu.suro.utils.math.Vector3D
import eu.suro.utils.math.Vector3Df
import io.netty.buffer.ByteBuf
import java.io.OutputStream
import java.util.*
import java.util.function.BiConsumer
import java.util.function.Consumer
import kotlin.collections.ArrayList


abstract class PacketWriter : OutputStream() {

    /**
     * Maximum string length in bytes allowed by the current protocol.
     */
    var MAX_STRING_LENGTH = 32767

    /**
     * Gets the size of the variable integer.
     *
     * @param value the integer value to calculate the variable size from
     * @return the variable size this value can be contained into, upto 5 bytes in total.
     */
    open fun getVarIntSize(value: Int): Int {
        for (j in 1..4) {
            if (value and (-1 shl j * 7) == 0) return j
        }
        return 5
    }


    /**
     * Gets the size of the variable long.
     *
     * @param value the long value to calculate the variable size from
     * @return the variable size this value can be contained into, upto 10 bytes in total.
     */
    open fun getVarLongSize(value: Long): Long {
        for (j in 1..9) {
            if (value and (-1L shl j * 7) == 0L) return j.toLong()
        }
        return 10
    }

    /**
     * The ByteBuf buffer that will be populated according to packet contents.
     */
    private val buffer: ByteBuf? = null

    /**
     * Additional packets that are required to be sent alongside other packets for proper functionality.
     */
    private var appendedPackets: List<AbstractPacket> = ArrayList()

    private val cancelled = false

    /**
     * Serializes a component to the buffer as a sized string.
     *
     * @param component the component to serialize
     */
    open fun writeComponent(component: Component?) {
        writeSizedString(if (component == null) "{\"text\":\"\"}" else component.toJavaJson())
    }

    /**
     * Writes a boolean to the buffer.
     *
     * @param b the boolean to Writes
     */
    open fun writeBoolean(b: Boolean) {
        buffer!!.writeBoolean(b)
    }

    /**
     * Writes a byte to the buffer.
     *
     * @param b the byte to write
     */
    open fun writeByte(b: Byte) {
        buffer!!.writeByte(b.toInt())
    }

    /**
     * Writes a char to the buffer.
     *
     * @param c the char to write
     */
    open fun writeChar(c: Char) {
        buffer!!.writeChar(c.code)
    }

    /**
     * Writes a short to the buffer.
     *
     * @param s the short to write
     */
    open fun writeShort(s: Int) {
        buffer!!.writeShort(s)
    }

    /**
     * Writes an int to the buffer.
     *
     * @param i the int to write
     */
    open fun writeInt(i: Int) {
        buffer!!.writeInt(i)
    }

    /**
     * Writes a long to the buffer.
     *
     * @param l the long to write
     */
    open fun writeLong(l: Long) {
        buffer!!.writeLong(l)
    }

    /**
     * Writes a float to the buffer.
     *
     * @param f the float to write
     */
    open fun writeFloat(f: Float) {
        buffer!!.writeFloat(f)
    }

    /**
     * Writes a double to the buffer.
     *
     * @param d the double to write
     */
    open fun writeDouble(d: Double) {
        buffer!!.writeDouble(d)
    }

    /**
     * Writes an int with variable size to the buffer.
     *
     * @param value the int to write
     */
    open fun writeVarInt(value: Int) {
        // Taken from velocity
        if (value and (-0x1 shl 7) == 0) {
            buffer!!.writeByte(value)
        } else if (value and (-0x1 shl 14) == 0) {
            val w = value and 0x7F or 0x80 shl 8 or (value ushr 7)
            buffer!!.writeShort(w)
        } else {
            writeVarIntFull(value)
        }
    }

    /**
     * Writes an int with variable size to the buffer.
     *
     * @param value the int to write
     */
    open fun writeVarIntFull(value: Int) {
        // Took from velocity
        if (value and (-0x1 shl 7) == 0) {
            buffer!!.writeByte(value)
        } else if (value and (-0x1 shl 14) == 0) {
            val w = value and 0x7F or 0x80 shl 8 or (value ushr 7)
            buffer!!.writeShort(w)
        } else if (value and (-0x1 shl 21) == 0) {
            val w = value and 0x7F or 0x80 shl 16 or (value ushr 7 and 0x7F or 0x80 shl 8) or (value ushr 14)
            buffer!!.writeMedium(w)
        } else {
            val w = value and 0x7F or 0x80 shl 24 or (value ushr 7 and 0x7F or 0x80 shl 16
                    ) or (value ushr 14 and 0x7F or 0x80 shl 8) or (value ushr 21 and 0x7F or 0x80)
            buffer!!.writeInt(w)
            buffer.writeByte(value ushr 28)
        }
    }

    /**
     * Writes a long with variable size to the buffer.
     *
     * @param value the long to write
     */
    open fun writeVarLong(value: Long) {
        var value = value
        do {
            var temp = (value and 127L).toByte()
            value = value ushr 7
            if (value != 0L) {
                temp = (temp.toInt() or 128).toByte()
            }
            writeByte(temp)
        } while (value != 0L)
    }

    /**
     * Writes a string with fixed size to the buffer.
     *
     * @param string to write
     */
    open fun writeSizedString(string: String) {
        writeSizedString(string, MAX_STRING_LENGTH)
    }

    /**
     * Writes a string with provided size to the buffer.
     *
     * @param string the string to write
     * @param maxLength the maximum length the string can hold
     */
    open fun writeSizedString(string: String, maxLength: Int) {
        val bytes: ByteArray = string.getBytes(StandardCharsets.UTF_8)
        if (bytes.size > maxLength) {
            throw UnsupportedOperationException("String too big! (is " + bytes.size + " bytes encoded, should be less than: " + maxLength + ")")
        }
        writeVarInt(bytes.size)
        buffer!!.writeBytes(bytes)
    }

    /**
     * Writes a string that is properly null terminated to the buffer.
     *
     * @param s the string to write
     */
//    open fun writeNullTerminatedString(s: String) {
//        buffer.writeCharSequence(s + '\u0000', StandardCharsets.UTF_8)
//    }

    /**
     * Writes a var-int array to the buffer.
     *
     * @param array the array to write
     */
    open fun writeVarIntArray(array: IntArray?) {
        if (array == null) {
            writeVarInt(0)
            return
        }
        writeVarInt(array.size)
        for (element in array) {
            writeVarInt(element)
        }
    }

    /**
     * Writes a var-long array to the buffer.
     *
     * @param array the array to write
     */
    open fun writeLongArray(array: LongArray?) {
        if (array == null) {
            writeVarInt(0)
            return
        }
        writeVarInt(array.size)
        for (element in array) {
            writeLong(element)
        }
    }

    /**
     * Writes a byte array to the buffer.
     *
     * @param bytes the byte array to write
     */
    open fun writeBytes(bytes: ByteArray?) {
        buffer!!.writeBytes(bytes)
    }

    /**
     * Writes a collection of string to the buffer.
     *
     * @param collection the string collection to write
     */
    open fun writeStringCollection(collection: Collection<String>?) {
        if (collection == null) {
            writeVarInt(0)
            return
        }
        writeVarInt(collection.size)
        for (element in collection) {
            writeSizedString(element)
        }
    }

    /**
     * Writes a string array to the buffer.
     *
     * @param array the string array to write
     */
    open fun writeStringArray(array: Array<String>?) {
        if (array == null) {
            writeVarInt(0)
            return
        }
        writeVarInt(array.size)
        for (element in array) {
            writeSizedString(element)
        }
    }

    /**
     * Writes a UUID to the buffer.
     *
     * @param uuid the uuid to write
     */
    open fun writeUuid(uuid: UUID) {
        writeLong(uuid.mostSignificantBits)
        writeLong(uuid.leastSignificantBits)
    }

    /**
     * Writes a block position to the buffer.
     *
     * @param location the location at which the block exists to write to the buffer
     */
    open fun writeBlockPosition(location: LocationHolder) {
        writeBlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ())
    }

    /**
     * Writes the offset between two locations to the buffer
     *
     * @param main the main location to calculate the offset from
     * @param location the location to find subtract to the main vector to calculate the offset from
     */
    open fun writeByteOffset(main: Vector3Df, location: Vector3Df) {
        writeByte((location.x - main.x) as Byte)
        writeByte((location.y - main.y) as Byte)
        writeByte((location.z - main.z) as Byte)
    }

    /**
     * Writes a block position to the buffer.
     *
     * @param x the x position of the block
     * @param y the y position of the block
     * @param z the z position of the block
     */
    open fun writeBlockPosition(x: Int, y: Int, z: Int) {
        writeLong(x.toLong() and 0x3FFFFFFL shl 38 or (z.toLong() and 0x3FFFFFFL shl 12) or (y.toLong() and 0xFFFL))
    }

    /**
     * Writes a Vector3D to the buffer.
     *
     * @param vector3D the vector3d instance to write to the buffer
     */
    open fun writeVector(vector3D: Vector3D) {
        writeDouble(vector3D.x)
        writeDouble(vector3D.y)
        writeDouble(vector3D.z)
    }

    /**
     * Writes a location vector to the buffer.
     *
     * @param locationHolder the location to write
     */
    open fun writeVector(locationHolder: LocationHolder) {
        writeVector(locationHolder.asVector())
    }

    /**
     * Writes a fixed point vector from a location with each axis multiplied by 32 to the buffer.
     *
     * @param locationHolder the location to write the fixed point vector
     */
    open fun writeFixedPointVector(locationHolder: LocationHolder) {
        writeFixedPointVector(locationHolder.asVector())
    }

    /**
     * Writes a fixed point vector with each axis of the vector multiplied by 32 to the buffer.
     *
     * @param vector3D the vector to write
     */
    open fun writeFixedPointVector(vector3D: Vector3D) {
        writeInt((vector3D.x * 32) as Int)
        writeInt((vector3D.y * 32) as Int)
        writeInt((vector3D.z * 32) as Int)
    }

    /**
     * Writes a ByteRotation (yaw/pitch) to the buffer.
     *
     * @param locationHolder the location from which to write the (yaw/pitch) from
     */
    open fun writeByteRotation(locationHolder: LocationHolder) {
        writeByteRotation(locationHolder.getYaw(), locationHolder.getPitch())
    }

    /**
     * Writes a ByteRotation to the buffer.
     *
     * @param yaw the yaw to write
     * @param pitch the pitch to write
     */
    open fun writeByteRotation(yaw: Float, pitch: Float) {
        writeByte((yaw * 256 / 360).toInt().toByte())
        writeByte((pitch * 256 / 360).toInt().toByte())
    }

    /**
     * Writes a Vector3Df to the buffer.
     *
     * @param vector3Df the vector to write
     */
    open fun writeVector(vector3Df: Vector3Df) {
        writeFloat(vector3Df.x)
        writeFloat(vector3Df.y)
        writeFloat(vector3Df.z)
    }

    /**
     * Writes a Vector3Df of motion to the buffer.
     * Note: Each axis is multiplied by 8000
     *
     * @param vector3D the motion vector to write
     */
    open fun writeMotion(vector3D: Vector3D) {
        writeShort((vector3D.x * 8000).toInt())
        writeShort((vector3D.y * 8000).toInt())
        writeShort((vector3D.z * 8000).toInt())
    }

    // TODO:
    // misat tf is this? fill jd for it
    open fun writeMove(vector3D: Vector3D) {
        writeShort((vector3D.x * 4096).toInt())
        writeShort((vector3D.y * 4096).toInt())
        writeShort((vector3D.z * 4096).toInt())
    }

    /**
     * Writes an Item to the buffer.
     *
     * @param item the item to write
     */
    open fun writeItem(item: Item) {
        if (item.getMaterial().isAir()) {
            if (protocol() >= 402) {
                writeBoolean(false)
            } else {
                writeShort(-1)
            }
        } else {
            if (protocol() >= 402) {
                writeBoolean(true)
                writeVarInt(getItemId(item.getMaterial()))
            } else {
                writeShort(getItemId(item.getMaterial()))
            }
            write(item.getAmount())
            if (protocol() < 351) {
                writeShort(item.getMaterial().forcedDurability())
            }
            writeNBTFromItem(item)
        }
    }

    open fun <T> writeSizedArray(array: Array<T>, consumer: Consumer<T>) {
        writeVarInt(array.size)
        for (a in array) {
            consumer.accept(a)
        }
    }

    open fun <T> writeSizedCollection(collection: Collection<T>, consumer: Consumer<T>) {
        writeVarInt(collection.size)
        for (a in collection) {
            consumer.accept(a)
        }
    }

    open fun <K, V> writeSizedMap(map: Map<K, V>, consumer: BiConsumer<K, V>) {
        writeVarInt(map.size)
        for ((key, value): Map.Entry<K, V> in map) {
            consumer.accept(key, value)
        }
    }

    open fun writeBlockData(blockDataHolder: BlockTypeHolder?) {
        writeVarInt(getBlockStateId(blockDataHolder))
    }

    open fun writeDataWatcherCollection(collection: Collection<MetadataItem?>) {
        for (item in collection) {
            item.write(this)
        }
        writeByte(0xff.toByte()) // termination sequence
    }

    // Platform classes must override this method
    open fun writeNBTFromItem(item: Item?) {
        write(0)
    }

    //public void writeNBT(String name, Object nbt) {
    // TODO: write nbt
    //}

    //public void writeNBT(String name, Object nbt) {
    // TODO: write nbt
    //}
    open fun append(packet: AbstractPacket?) {
        appendedPackets.add(packet)
    }

    override fun write(b: Int) {
        writeByte(b.toByte())
    }

    open fun protocol(): Int {
        return Server.getProtocolVersion()
    }

    protected abstract fun getItemId(material: ItemTypeHolder?): Int

    protected abstract fun getBlockStateId(blockDataHolder: BlockTypeHolder?): Int

    abstract fun getEquipmentSlotId(equipmentSlotHolder: EquipmentSlotHolder?): Int
}