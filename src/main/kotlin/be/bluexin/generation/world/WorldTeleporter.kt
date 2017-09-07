package be.bluexin.generation.world

import be.bluexin.generation.blocks.BlockPortal
import be.bluexin.generation.util.isOurs
import be.bluexin.generation.util.set
import be.bluexin.generation.util.setPosition
import com.teamwizardry.librarianlib.features.kotlin.get
import com.teamwizardry.librarianlib.features.saving.serializers.builtin.basics.SerializeBlockPos
import net.minecraft.entity.Entity
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.math.BlockPos
import net.minecraft.world.Teleporter
import net.minecraft.world.WorldServer


/**
 * Part of generationtests by bluexin, released under GNU GPLv3.
 *
 * @author bluexin
 */
class WorldTeleporter(worldIn: WorldServer) : Teleporter(worldIn) {

    override fun makePortal(entityIn: Entity): Boolean {
//        println("makePortal $entityIn")

        this.world.setBlockState(portalPos, BlockPortal.defaultState)
        return true
    }

    override fun placeInPortal(entityIn: Entity, rotationYaw: Float) {
//        println("Place in Portal $entityIn")

        if (this.world.isOurs) {
            if (!hasPortal()) makePortal(entityIn)
        }
        placeInExistingPortal(entityIn, rotationYaw)
    }

    override fun placeInExistingPortal(entityIn: Entity, rotationYaw: Float): Boolean {
        if (this.world.isOurs) {
            if (!entityIn.world.isOurs) storePosition(entityIn)
            entityIn.setPosition(portalPos.up())
        } else if (entityIn.world.isOurs) restorePosition(entityIn)
        return true
    }

    private fun restorePosition(entityIn: Entity) {
        val atag = entityIn.entityData
        val tag = (atag[TAG_NAME] as? NBTTagCompound)?.get("world${this.world.provider.dimension}") ?: return
        entityIn.setPosition(SerializeBlockPos.read(tag, null, false))
//        println("Set ${entityIn.position} to ${entityIn.name}")
    }

    private fun storePosition(entityIn: Entity) {
        val atag = entityIn.entityData
        val tag = atag[TAG_NAME] as? NBTTagCompound? ?: NBTTagCompound()
        tag["world${entityIn.world.provider.dimension}"] = SerializeBlockPos.write(entityIn.position, false)
        atag[TAG_NAME] = tag
//        println("Saved ${entityIn.position} for ${entityIn.name} in world${entityIn.world.provider.dimension}")
    }

    private fun hasPortal() = this.world.isOurs && world.getBlockState(portalPos).block == BlockPortal

    companion object {
        val portalPos = BlockPos(0, 60, 0)
        const val TAG_NAME = "gentests"
    }
}
