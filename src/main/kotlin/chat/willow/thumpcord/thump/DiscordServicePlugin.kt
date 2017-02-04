package chat.willow.thumpcord.thump

import chat.willow.thumpcord.Thumpcord
import engineer.carrot.warren.thump.api.*
import io.drakon.thumpcord.Thumpcord.log
import chat.willow.thumpcord.discord.Discord

import io.drakon.thumpcord.thump.config.DiscordServicePluginConfig
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentString
import org.apache.logging.log4j.Level

/**
 * Discord Thump service plugin. Based on the original IRC adapter.
 */
@ThumpServicePlugin
object DiscordServicePlugin: IThumpServicePlugin {

    override val id = "discord"
    override val commandHandler: ICommandHandler = DiscordServiceCommandHandler

    private val formatter: IServiceChatFormatter = DiscordChatFormatter
    lateinit var configuration: DiscordServicePluginConfig
    lateinit var sink: IThumpMinecraftSink
    private var started = false

    override fun configure(context: ThumpPluginContext) {
        val conf = context.configuration
        sink = context.minecraftSink

        configuration = DiscordServicePluginConfig(conf)
        configuration.load()
        configuration.save()
    }

    override fun anyConnectionsMatch(name: String): Boolean {
        return false
    }

    override fun status(): List<String> {
        return emptyList()
    }

    override fun start() {
        if (started) {
            Thumpcord.log.warn("Thumpcord already started! Skipping.")
            return
        }
        if (configuration.account.token == "") {
            Thumpcord.log.info("No token provided; skipping Discord integration.")
            return
        }
        Thumpcord.log.info("Thumpcord start()")
        try {
            val success = Discord.connect(configuration)
            if (!success) {
                Thumpcord.log.warn("Unable to connect to Discord! Use '/thump discord reconnect' to reconnect.")
                sink.sendToAllPlayersWithoutCheckingSource(TextComponentString("[Thumpcord] Unable to connect to Discord! Use '/thump discord reconnect' to reconnect."))
            }
        } catch (ex: Discord.DiscordAlreadyConnectedException) {
            throw RuntimeException("Discord already running?!", ex)
        }
    }

    fun reconnect() {
        if (started && !Discord.isConnected) {
            Thumpcord.log.info("Attempting to reconnect to Discord...")
            sink.sendToAllPlayersWithoutCheckingSource(TextComponentString("[Thumpcord] Attempting to reconnect to Discord..."))
            Discord.connect(configuration)
        }
    }

    override fun stop() {
        Discord.disconnect()
        started = false
    }

    override fun onMinecraftMessage(message: ITextComponent) {
        Discord.sendToChannel(DiscordChatFormatter.format(message))
    }

}