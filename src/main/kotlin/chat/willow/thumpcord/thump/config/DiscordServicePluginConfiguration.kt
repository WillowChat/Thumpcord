package chat.willow.thumpcord.thump.config

import engineer.carrot.warren.thump.api.IThumpServicePluginConfig
import net.minecraftforge.common.config.Configuration

/**
 * Based on the original Thump IRC plugin config class.
 */
class DiscordServicePluginConfig(val conf: Configuration): IThumpServicePluginConfig {

    lateinit var general: DiscordServicePluginGeneralConfig
    lateinit var account: DiscordServicePluginAccountConfig
    lateinit var server: DiscordServicePluginServerConfig

    override fun load() {
        conf.load()
        general = DiscordServicePluginGeneralConfig(conf)
        account = DiscordServicePluginAccountConfig(conf)
        server = DiscordServicePluginServerConfig(conf)
    }

    override fun save() {
        conf.save()
    }

}

class DiscordServicePluginGeneralConfig(conf: Configuration) {
    var logDiscordMsgsToConsole = true
    var promoteDebugOutputToInfo = false

    init {
        conf.setCategoryPropertyOrder(CATEGORY, arrayListOf(LOG_TO_CONSOLE, LOG_DEBUG_TO_CONSOLE))
        logDiscordMsgsToConsole = conf.getBoolean(LOG_TO_CONSOLE, CATEGORY, logDiscordMsgsToConsole, "")
        promoteDebugOutputToInfo = conf.getBoolean(LOG_DEBUG_TO_CONSOLE, CATEGORY, promoteDebugOutputToInfo, LOG_DEBUG_TO_CONSOLE_COMMENT)
    }

    companion object {
        private val CATEGORY = "general"
        private val LOG_TO_CONSOLE = "LogDiscordToServerConsole"
        private val LOG_DEBUG_TO_CONSOLE = "LogDiscordDebugToConsole"
        private val LOG_DEBUG_TO_CONSOLE_COMMENT = "Leave disabled unless asked otherwise; outputs extra Discord debug information to console (at INFO) - when disabled, logs at DEBUG (which can be viewed with a modified log4j config)"
    }
}

class DiscordServicePluginAccountConfig(conf: Configuration) {
    //var appID = ""
    var token = ""

    init {
        conf.setCategoryPropertyOrder(CATEGORY, arrayListOf(TOKEN))
        //appID = conf.getString(APPID, CATEGORY, appID, APPID_COMMENT)
        token = conf.getString(TOKEN, CATEGORY, token, TOKEN_COMMENT)
    }

    companion object {
        private val CATEGORY = "account"
        //private val APPID = "AppID"
        //private val APPID_COMMENT = "Referred to as 'Client ID' (NOT 'Bot ID') in Discord applications interface."
        private val TOKEN = "Token"
        private val TOKEN_COMMENT = "This is the Token field under 'App Bot User' in Discord applications interface."
    }
}

class DiscordServicePluginServerConfig(conf: Configuration) {
    var serverID = "0123456789"
    var channel = "example"

    init {
        conf.setCategoryPropertyOrder(CATEGORY, arrayListOf(SERVERID, CHANNEL))
        serverID = conf.getString(SERVERID, CATEGORY, serverID, SERVERID_COMMENT)
        channel = conf.getString(CHANNEL, CATEGORY, channel, CHANNEL_COMMENT)
    }

    companion object {
        private val CATEGORY = "server"
        private val SERVERID = "ServerID"
        private val SERVERID_COMMENT = "Turn on Appearance -> Developer Mode in Discord settings, then right-click your server and hit Copy ID to get this."
        private val CHANNEL = "Channel"
        private val CHANNEL_COMMENT = "The channel name to bridge with, without the # prefix."
    }
}