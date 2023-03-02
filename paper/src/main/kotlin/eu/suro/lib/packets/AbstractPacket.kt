package eu.suro.lib.packets

import org.bukkit.entity.Player


/**
 * Represents a packet which can be sent to a player using [PacketMapper.sendPacket] or [AbstractPacket.sendPacket].
 */
abstract class AbstractPacket {
    /**
     * Writes the contents of this packet to an [PacketWriter] instance.
     *
     * Note that the writer must have written the packet id at the header before this method is called,
     * packets may not be functional otherwise.
     *
     * @param writer the writer to populate packet contents
     */
    abstract fun write(writer: PacketWriter?)

    /**
     * Sends the packet to the client the player is currently connected with.
     *
     * @param player the player to send the packet to
     */
    fun sendPacket(player: Player?) {
        player.sendp
        PacketMapper.sendPacket(player, this)
    }

    /**
     * Sends the same packet to multiple players of the list provided.
     *
     * @param players the players to send the packet to
     */
    fun sendPacket(players: Collection<PlayerWrapper?>?) {
        PacketMapper.sendPacket(players, this)
    }

    val id: Int
        /**
         * Gets the id of this packet.
         *
         * @return the id of this packet
         */
        get() = PacketMapper.getId(this.javaClass)
}