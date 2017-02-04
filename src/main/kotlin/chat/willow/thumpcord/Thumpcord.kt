package chat.willow.thumpcord

import chat.willow.thumpcord.minecraft.ThumpcordCommand
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.event.FMLServerStartingEvent
import org.apache.logging.log4j.LogManager
import sx.blah.discord.Discord4J

/**
 * Thumpcord - Discord adapter for Thump.
 *
 * @author Arkan <arkan@drakon.io>
 */
@Mod(modid = "thumpcord", name = "Thumpcord", modLanguage = "kotlin", modLanguageAdapter = "chat.willow.thumpcord.lib.KotlinAdapter", acceptableRemoteVersions = "*", acceptedMinecraftVersions = "[1.10]", dependencies = "required-after:Thump")
object Thumpcord {

    val log = LogManager.getLogger("Thumpcord")!!

    @Mod.EventHandler
    fun preinit(evt: FMLPreInitializationEvent) {
        log.info("Preinit; disabling Discord voice support.")
        Discord4J.audioDisabled.set(true)
    }

    @Mod.EventHandler
    fun serverStarting(evt: FMLServerStartingEvent) {
        log.info("Registering Thumpcord command.")
        evt.registerServerCommand(ThumpcordCommand())
    }

}