package chat.willow.thumpcord.thump

import chat.willow.thump.api.ICommandHandler
import net.minecraft.command.ICommandSender
import net.minecraft.util.text.TextComponentString

object DiscordServiceCommandHandler: ICommandHandler {
    private val command: String = "discord"
    private val usage: String = "/thump service discord reconnect"

    override fun addTabCompletionOptions(sender: ICommandSender, parameters: Array<String>): List<String> {
        return if (parameters.isEmpty() || "reconnect".startsWith(parameters.first(), true)) { listOf("reconnect") } else { emptyList() }
    }

    override fun processParameters(sender: ICommandSender, parameters: Array<String>) {
        if (parameters.isEmpty() || parameters.first().toLowerCase() != "reconnect") {
            sender.sendMessage(TextComponentString("Invalid usage."))
            sender.sendMessage(TextComponentString("  Usage: $usage"))
            return
        }
    }

    override fun getUsage(): String {
        return usage
    }

    override fun getCommand(): String {
        return command
    }
}