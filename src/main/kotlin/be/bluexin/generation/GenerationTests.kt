package be.bluexin.generation

import be.bluexin.generation.blocks.BlockPortal
import be.bluexin.generation.world.World1Dim
import com.teamwizardry.librarianlib.features.config.EasyConfigHandler
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.event.FMLServerStartingEvent

/**
 * Part of generationtests by Bluexin, released under GNU GPLv3.
 *
 * @author Bluexin
 */
@Mod(
        modid = GenerationTests.MODID,
        name = GenerationTests.MODNAME,
        version = GenerationTests.VERSION,
        dependencies = GenerationTests.DEPS,
        acceptedMinecraftVersions = GenerationTests.MC_VERSION
)
object GenerationTests {

    const val MODID = "gentests"
    const val MODNAME = "Generation Tests"
    const val VERSION = "0.0"
    const val DEPS = "required-after:librarianlib@[3.1.5,)"
    const val MC_VERSION = "[1.11,)"

    @JvmStatic
    @Mod.InstanceFactory
    fun shenanigan() = this

    @Mod.EventHandler
    fun preInit(e: FMLPreInitializationEvent) {
        EasyConfigHandler.init()
        // Register world generator

        BlockPortal
    }

    @Mod.EventHandler
    fun onServerLoad(e: FMLServerStartingEvent) {
        e.registerServerCommand(CommandTpw)

        World1Dim.register()
        // Register Dimension, DimensionType, Commands
    }
}
