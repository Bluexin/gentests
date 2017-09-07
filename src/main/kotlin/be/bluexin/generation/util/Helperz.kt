package be.bluexin.generation.util

import be.bluexin.generation.Orientation
import com.teamwizardry.librarianlib.features.kotlin.component1
import com.teamwizardry.librarianlib.features.kotlin.component2
import com.teamwizardry.librarianlib.features.kotlin.component3
import com.teamwizardry.librarianlib.features.structure.Structure
import net.minecraft.block.properties.IProperty
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i
import net.minecraft.world.chunk.ChunkPrimer


/**
 * Part of generationtests by bluexin, released under GNU GPLv3.
 *
 * @author bluexin
 */

/**
 * Sets an [Entity]'s position based on a [Vec3i] (including [BlockPos].
 * If [middle] is set to true (default) 0.5 will be added to x and z coordinates,
 * effectively positioning the [Entity] in the middle of the block.
 */
fun Entity.setPosition(pos: Vec3i, middle: Boolean = true) {
    val (x, y, z) = pos
    this.setPosition(if (middle) x + 0.5 else x.toDouble(), y.toDouble(), if (middle) z + 0.5 else z.toDouble())
}

operator fun NBTTagCompound.set(key: String, value: NBTBase) = this.setTag(key, value)

fun ChunkPrimer.setBlockState(pos: BlockPos, state: IBlockState) = this.setBlockState(pos.x, pos.y, pos.z, state)

fun ChunkPrimer.loadStructure(structure: Structure, offsetY: Int = 0, rotation: Orientation = Orientation.N) {
    val bb = AxisAlignedBB(0.0, 0.0, 0.0, 15.0, 255.0, 15.0)
    //if (structure.min in bb && structure.max in bb) structure.blockInfos().forEach { this.setBlockState(it.pos.up(offsetY), it.blockState) }
    //else throw IndexOutOfBoundsException("Structure out of bounds!") // TODO: use this when this is not borked anymore

    when (rotation) {
        Orientation.N -> structure.blockInfos().forEach { this.setBlockState(it.pos.up(offsetY), it.blockState.rotate(rotation)) }
        Orientation.E -> structure.blockInfos().forEach { this.setBlockState(BlockPos(15 - it.pos.z, it.pos.y + offsetY, it.pos.x), it.blockState.rotate(rotation)) }
        Orientation.S -> structure.blockInfos().forEach { this.setBlockState(BlockPos(15 - it.pos.x, it.pos.y + offsetY, 15 - it.pos.z), it.blockState.rotate(rotation)) }
        Orientation.W -> structure.blockInfos().forEach { this.setBlockState(BlockPos(it.pos.z, it.pos.y + offsetY, 15 - it.pos.x), it.blockState.rotate(rotation)) }
    }
}

private fun IBlockState.rotate(rotation: Orientation): IBlockState {
    var bs = this
    this.properties.filterKeys { it.getValueClass() == EnumFacing::class.java }
            .forEach { prop, oldValue ->
                var newFacing = oldValue as EnumFacing
                (0 until rotation.ordinal).forEach { newFacing = newFacing.rotateAround(EnumFacing.Axis.Y) }

                @Suppress("UNCHECKED_CAST")
                if (newFacing in prop.getAllowedValues()) bs = bs.withProperty(prop as IProperty<EnumFacing>, newFacing)
            }
    return bs
}

fun ChunkPrimer.loadStructure(structure: Pair<Structure, Orientation>, offsetY: Int = 0) = this.loadStructure(structure.first, offsetY, structure.second)

operator fun AxisAlignedBB.contains(vec3i: Vec3i)
        = vec3i.x >= this.minX && vec3i.x <= this.maxX
        && vec3i.y >= this.minY && vec3i.y <= this.maxY
        && vec3i.z >= this.minZ && vec3i.z <= this.maxZ
