package io.drakon.thumpcord.minecraft

import io.drakon.thumpcord.discord.Discord
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.server.MinecraftServer
import net.minecraft.util.text.TextComponentString
import net.minecraftforge.common.ForgeHooks

class ThumpcordCommand: CommandBase() {

    override fun execute(server: MinecraftServer?, sender: ICommandSender?, args: Array<out String>?) {
        if (args?.firstOrNull()?.toLowerCase() == "invite") {
            if (Discord.isConnected) {
                val link = Discord.getInviteLink()
                val txt = TextComponentString("Bot invite link: ")
                txt.appendSibling(ForgeHooks.newChatWithLinks(link))
                sender?.addChatMessage(txt)
            }
        }
    }

    override fun getCommandName(): String {
        return "thumpcord"
    }

    override fun getCommandUsage(sender: ICommandSender?): String {
        return "/thumpcord invite"
    }

}