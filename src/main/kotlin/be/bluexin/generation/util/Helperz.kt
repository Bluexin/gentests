package be.bluexin.generation.util

import com.teamwizardry.librarianlib.features.kotlin.component1
import com.teamwizardry.librarianlib.features.kotlin.component2
import com.teamwizardry.librarianlib.features.kotlin.component3
import net.minecraft.entity.Entity
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3i


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
