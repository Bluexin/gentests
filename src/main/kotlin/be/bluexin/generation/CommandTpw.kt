package be.bluexin.generation

import be.bluexin.generation.util.StructureLoader
import be.bluexin.generation.world.WorldTeleporter
import com.teamwizardry.librarianlib.features.kotlin.sendSpamlessMessage
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.command.NumberInvalidException
import net.minecraft.command.WrongUsageException
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.server.MinecraftServer
import net.minecraft.util.text.TextComponentTranslation
import net.minecraftforge.common.DimensionManager

/**
 * Part of generationtests by Bluexin, released under GNU GPLv3.
 *
 * @author Bluexin
 */
object CommandTpw : CommandBase() {
    const val MSG_CHANNEL = "gentesttpw"

    override fun getName() = "tpw"

    override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<out String>) {
        if (args.isNotEmpty() && args[0] == "print") {
            StructureLoader.printGrid()
            return
        }

        val target = if (args.size == 2) getPlayer(server, sender, args[0]) else getCommandSenderAsPlayer(sender)
        val worldid = if (args.size == 1) parseInt(args[0]) else if (args.size == 2) parseInt(args[1]) else throw WrongUsageException(getUsage(sender))
        if (!DimensionManager.isDimensionRegistered(worldid)) throw NumberInvalidException()
        if (target.dimension == worldid) throw NumberInvalidException("gentests:tpw.current", worldid)

        val newWorld = server.getWorld(worldid)
        if (sender is EntityPlayer) sender.sendSpamlessMessage(TextComponentTranslation("gentests:tpw.success", target.name, newWorld.provider.dimensionType.getName(), worldid), MSG_CHANNEL)
        else sender.sendMessage(TextComponentTranslation("gentests:tpw.success", target.name, newWorld.provider.dimensionType.getName(), worldid))

        server.playerList.transferPlayerToDimension(target, worldid, WorldTeleporter(newWorld))
    }

    override fun getUsage(sender: ICommandSender) = "gentests:tpw.usage"

    override fun getRequiredPermissionLevel() = 2

    override fun isUsernameIndex(args: Array<out String>, index: Int) = index == 0
}
