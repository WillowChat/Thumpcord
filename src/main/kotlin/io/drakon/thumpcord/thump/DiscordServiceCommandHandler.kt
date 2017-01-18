package io.drakon.thumpcord.thump

import engineer.carrot.warren.thump.api.ICommandHandler
import net.minecraft.command.ICommandSender
import net.minecraft.util.text.TextComponentString

object DiscordServiceCommandHandler: ICommandHandler {
    override val command: String = "discord"
    override val usage: String = "/thump service discord reconnect"

    override fun addTabCompletionOptions(sender: ICommandSender, parameters: Array<String>): List<String> {
        return if (parameters.isEmpty() || "reconnect".startsWith(parameters.first(), true)) { listOf("reconnect") } else { emptyList() }
    }

    override fun processParameters(sender: ICommandSender, parameters: Array<String>) {
        if (parameters.isEmpty() || parameters.first().toLowerCase() != "reconnect") {
            sender.addChatMessage(TextComponentString("Invalid usage."))
            sender.addChatMessage(TextComponentString("  Usage: $usage"))
            return
        }
    }
}