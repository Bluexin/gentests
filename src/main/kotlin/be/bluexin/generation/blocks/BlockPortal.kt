package be.bluexin.generation.blocks

import be.bluexin.generation.CommandTpw
import be.bluexin.generation.util.isOurs
import be.bluexin.generation.world1.WorldTeleporter
import com.teamwizardry.librarianlib.features.base.block.BlockMod
import com.teamwizardry.librarianlib.features.kotlin.sendSpamlessMessage
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentTranslation
import net.minecraft.world.World

/**
 * Part of generationtests by bluexin, released under GNU GPLv3.
 *
 * @author bluexin
 */
object BlockPortal : BlockMod("portal", Material.ROCK) {

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float) =
            if (worldIn.isOurs) {
                if (!worldIn.isRemote) {
                    val server = worldIn.minecraftServer!!
                    val newWorld = server.getWorld(0)
                    playerIn.sendSpamlessMessage(TextComponentTranslation("gentests:tpw.success", playerIn.name, newWorld.provider.dimensionType.getName(), 0), CommandTpw.MSG_CHANNEL)

                    server.playerList.transferPlayerToDimension(playerIn as EntityPlayerMP, 0, WorldTeleporter(newWorld))
                }

                true
            } else false
}