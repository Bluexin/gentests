package be.bluexin.generation.util

import net.minecraft.world.DimensionType
import net.minecraft.world.World
import net.minecraftforge.common.DimensionManager

/**
 * Part of generationtests by Bluexin, released under GNU GPLv3.
 *
 * @author Bluexin
 */
object WorldUtils {
    private val genTestDims = mutableMapOf<Int, DimensionType>()

    fun registerDimension(id: Int, type: DimensionType) { // Only checks for our dims. DimensionManager checks overall.
        if (id in genTestDims) throw IllegalArgumentException("ID $id requested by ${type.getName()} already used by ${genTestDims[id]?.getName()}")
        if (!DimensionManager.isDimensionRegistered(id)) DimensionManager.registerDimension(id, type)
        genTestDims[id] = type
    }

    fun isOurs(id: Int) = id in genTestDims
}

val World.isOurs
    get() = WorldUtils.isOurs(this.provider.dimension)
