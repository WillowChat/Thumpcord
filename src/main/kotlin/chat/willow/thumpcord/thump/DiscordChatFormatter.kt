package chat.willow.thumpcord.thump

import engineer.carrot.warren.thump.api.IServiceChatFormatter
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentString

/**
 * Identical to the IRC one - just wraps/unwraps strings.
 */
object DiscordChatFormatter: IServiceChatFormatter {

    override fun format(plaintext: String): ITextComponent {
        return TextComponentString(plaintext)
    }

    override fun format(text: ITextComponent): String {
        return text.unformattedText
    }

}