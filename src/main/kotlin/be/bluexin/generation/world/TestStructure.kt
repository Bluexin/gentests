package be.bluexin.generation.world

import be.bluexin.generation.GenerationTests
import be.bluexin.generation.util.setBlockState
import com.teamwizardry.librarianlib.features.structure.Structure
import net.minecraft.block.BlockStoneBrick
import net.minecraft.init.Blocks
import net.minecraft.util.ResourceLocation
import net.minecraft.world.chunk.ChunkPrimer


/**
 * Part of generationtests by bluexin, released under GNU GPLv3.
 *
 * @author bluexin
 */
object TestStructure {

    private val DATA = arrayOf(
            intArrayOf(0, 0, 0, 0, 1, 2, 3, 3, 3, 3, 2, 1, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 1, 2, 3, 3, 3, 3, 2, 1, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 1, 2, 3, 3, 3, 3, 2, 1, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 1, 2, 3, 3, 3, 3, 2, 1, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 1, 2, 3, 3, 3, 3, 2, 1, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 1, 2, 3, 3, 3, 3, 2, 1, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 1, 2, 3, 3, 3, 3, 2, 1, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 1, 2, 3, 3, 3, 3, 2, 1, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 1, 2, 3, 3, 3, 3, 2, 1, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 1, 2, 3, 3, 3, 3, 2, 1, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 1, 2, 3, 3, 3, 3, 2, 1, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 1, 2, 3, 3, 3, 3, 2, 1, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 1, 2, 3, 3, 3, 3, 2, 1, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 1, 2, 3, 3, 3, 3, 2, 1, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 1, 2, 3, 3, 3, 3, 2, 1, 0, 0, 0, 0, 0),
            intArrayOf(0, 0, 0, 0, 1, 2, 3, 3, 3, 3, 2, 1, 0, 0, 0, 0)
    )

    // Beware: this might need lateinit if getting classloaded by something
    private val STATES = arrayOf(
            Blocks.STONEBRICK.defaultState,
            Blocks.STONEBRICK.defaultState.withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED),
            Blocks.STONE.defaultState,
            Blocks.COBBLESTONE.defaultState
    )

    private val STRUCTURE = Structure(ResourceLocation(GenerationTests.MODID, "chunkz")).blockInfos()

    init {
        if (DATA.size != 16 || DATA.any { it.size != 16 }) throw IllegalStateException("Invalid DATA !")
    }

    fun genC1(chunkX: Int, chunkZ: Int, primer: ChunkPrimer) {
        DATA.forEachIndexed { x, it ->
            it.forEachIndexed { z, it ->
                primer.setBlockState(x, 60, z, STATES[it])
            }
        }
        STRUCTURE.forEach { primer.setBlockState(it.pos.up(61), it.blockState) } // TODO: support for TEs
    }
}
