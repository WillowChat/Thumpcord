package io.drakon.thumpcord.discord

import io.drakon.thumpcord.Thumpcord.log
import io.drakon.thumpcord.thump.DiscordChatFormatter
import io.drakon.thumpcord.thump.DiscordServicePlugin.configuration
import io.drakon.thumpcord.thump.DiscordServicePlugin.sink
import net.minecraft.util.text.TextComponentString
import net.minecraftforge.fml.server.FMLServerHandler
import org.apache.logging.log4j.Level
import sx.blah.discord.api.events.EventSubscriber
import sx.blah.discord.handle.impl.events.*

/**
 * Event handlers for Discord.
 */
object DiscordEventHandler {

    @EventSubscriber
    fun ready(evt: ReadyEvent) {
        debugLog("Ready: client ID '{}'", evt.client.applicationClientID)
        sink.sendToAllPlayersWithoutCheckingSource(TextComponentString("[Thumpcord] Discord connected!"))
    }

    @EventSubscriber
    fun guildUnavailable(evt: GuildUnavailableEvent) {
        debugLog("GuildUnavailable: {}", evt.guildID)
        if (evt.guildID == configuration.server.channel) {
            sink.sendToAllPlayersWithoutCheckingSource(TextComponentString("[Thumpcord] Discord guild disconnected. This is usually temporary."))
        }
    }

    @EventSubscriber
    fun messageReceived(evt: MessageReceivedEvent) {
        val msg = evt.message
        val displayName = msg.author.getDisplayName(evt.message.guild)
        debugLog("MessageReceived: User {}/'{}', guild '{}', channel '{}', message '{}'",
                msg.author.name, displayName, msg.guild.id, msg.channel.name, msg.content)
        if (msg.guild.id == configuration.server.serverID && msg.channel.name == configuration.server.channel) {
            if (msg.content == "!players") {
                try {
                    val serv = FMLServerHandler.instance().server
                    val users = serv.allUsernames
                    if (users.isEmpty()) {
                        msg.reply("Nobody is online.")
                    } else {
                        val online = users.joinToString(", ")
                        msg.reply("Players online: $online")
                    }
                } catch (ex: NullPointerException) {
                    // NO-OP
                }
            } else {
                sink.sendToAllPlayers(displayName, DiscordChatFormatter.format(">> [$displayName] ${msg.content}"))
            }
        }
    }

    @EventSubscriber
    fun reconnectFailure(evt: ReconnectFailureEvent) {
        sink.sendToAllPlayersWithoutCheckingSource(TextComponentString("[Thumpcord] Reconnect to Discord failed (attempt #${evt.curAttempt})"))
        debugLog("ReconnectFailure: shard {}/{}, guilds on shard: {} (attempt #{})",
                evt.shard.info[0], evt.shard.info[1], evt.shard.guilds.joinToString(", "), evt.curAttempt)
    }

    @EventSubscriber
    fun reconnectSuccess(evt: ReconnectSuccessEvent) {
        sink.sendToAllPlayersWithoutCheckingSource(TextComponentString("[Thumpcord] Reconnected to Discord!"))
        debugLog("ReconnectSuccess: shard {}/{}, guilds on shard: {}",
                evt.shard.info[0], evt.shard.info[1], evt.shard.guilds.joinToString(", "))
    }

    private fun debugLog(msg: String, vararg objs: Any) {
        val lvl = if (configuration.general.promoteDebugOutputToInfo) Level.INFO else Level.DEBUG
        if (objs.isEmpty()) log.log(lvl, msg) else log.log(lvl, msg, *objs)
    }

}