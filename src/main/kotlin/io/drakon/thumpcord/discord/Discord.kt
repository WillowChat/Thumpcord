package io.drakon.thumpcord.discord

import io.drakon.thumpcord.thump.config.DiscordServicePluginConfig
import org.apache.logging.log4j.LogManager
import sx.blah.discord.api.ClientBuilder
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.handle.obj.IVoiceChannel
import sx.blah.discord.handle.obj.Permissions
import sx.blah.discord.util.BotInviteBuilder
import sx.blah.discord.util.DiscordException
import java.util.*

/**
 * Main entrypoint for Discord handling.
 */
object Discord {

    private var client: IDiscordClient? = null

    private var guildID = ""
    private var channelName = ""

    val logger = LogManager.getLogger("Thumpcord/Discord")

    val isConnected: Boolean
        get() = client?.isLoggedIn ?: false

    /**
     * Creates and connects a Discord client connection.
     * @param conf The Discord Thump configuration.
     */
    fun connect(conf: DiscordServicePluginConfig): Boolean {
        if (isConnected) throw DiscordAlreadyConnectedException()
        guildID = conf.server.serverID
        channelName = conf.server.channel
        val builder = ClientBuilder()
        builder.withToken(conf.account.token)
        try {
            client = builder.build()
            client?.dispatcher?.registerListener(DiscordEventHandler)
            client?.login()
            return true
        } catch (ex: DiscordException) {
            logger.error("Exception caught while building DiscordClient", ex)
            return false
        }
    }

    /**
     * Disconnect the current client, if it exists.
     */
    fun disconnect() {
        client?.logout()
        client = null
    }

    fun getInviteLink(): String {
        if (!isConnected) throw DiscordNotConnectedException()
        val builder = BotInviteBuilder(client)
        builder.withPermissions(EnumSet.of(Permissions.READ_MESSAGES, Permissions.SEND_MESSAGES, Permissions.EMBED_LINKS, Permissions.MENTION_EVERYONE))
        return builder.build()
    }

    fun sendToChannel(msg: String) {
        try {
            val channel = getChannel()
            channel?.sendMessage(msg)
        } catch (ex: DiscordNotConnectedException) {}
    }

    private fun getGuild(): IGuild? {
        if (!isConnected) throw DiscordNotConnectedException()
        return client?.getGuildByID(guildID)
    }

    private fun getChannel(): IChannel? {
        if (!isConnected) throw DiscordNotConnectedException()
        val channels = getGuild()?.getChannelsByName(channelName) ?: emptyList<IChannel>()
        // Filter out voice channels (we don't care about those)
        return channels.filterNot { it is IVoiceChannel }.firstOrNull()
    }

    class DiscordNotConnectedException: Exception()
    class DiscordAlreadyConnectedException: Exception()
}