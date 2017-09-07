package be.bluexin.generation.world

import be.bluexin.generation.to
import be.bluexin.generation.util.StructureLoader
import be.bluexin.generation.util.WorldUtils
import be.bluexin.generation.util.loadStructure
import com.teamwizardry.librarianlib.features.config.ConfigProperty
import net.minecraft.entity.EnumCreatureType
import net.minecraft.init.Blocks
import net.minecraft.util.math.BlockPos
import net.minecraft.world.DimensionType
import net.minecraft.world.World
import net.minecraft.world.WorldProvider
import net.minecraft.world.biome.Biome
import net.minecraft.world.biome.BiomeProvider
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.chunk.ChunkPrimer
import net.minecraft.world.chunk.IChunkGenerator
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.util.*
import java.util.Collections.emptyList

/**
 * Part of generationtests by Bluexin, released under GNU GPLv3.
 *
 * @author Bluexin
 */
object World1Dim { // TODO: use DimensionManager.getNextFreeDimId, and save the result to config (once liblib allows saving to config kek)

    @ConfigProperty(category = "dimIDs", comment = "ID for world1")
    var world1ID: Int = 101
        private set

    val type = DimensionType.register("World 1", "_world1", world1ID, World1Provider::class.java, false)!!

    fun register() {  // Not in init because liblib likes to classload early ;p
        if (!WorldUtils.isOurs(world1ID)) WorldUtils.registerDimension(world1ID, type)
    }
}

class World1Provider : WorldProvider() {
    override fun getDimensionType() = World1Dim.type

    override fun createChunkGenerator() = World1Generator(world)

    override fun getBiomeProvider(): BiomeProvider {
        return super.getBiomeProvider()
    }

    override fun getBiomeForCoords(pos: BlockPos?): Biome {
        return super.getBiomeForCoords(pos)
    } // TODO: custom biome

    override fun canRespawnHere() = false

    override fun canDoRainSnowIce(chunk: Chunk?) = false

    @SideOnly(Side.CLIENT)
    override fun getCloudHeight() = -10000f

    override fun isSurfaceWorld() = false

    override fun getHeight() = 128
}

class World1Generator(private val world: World) : IChunkGenerator {

    init {
        StructureLoader.init(Random(world.seed))
    }

    private fun isChunkCenter(chunkX: Int, chunkZ: Int): Boolean {
        val s2 = (chunkX.toLong() + world.seed + 337) * 947 + chunkZ * 719L
        val rand = Random(s2)
        rand.nextFloat()
        return rand.nextFloat() < .3f
    }

    private fun getRandomForChunk(chunkX: Int, chunkZ: Int): Random {
        val s2 = (chunkX.toLong() + world.seed + 13) * 314 + chunkZ * 17L
        val rand = Random(s2)
        rand.nextFloat()
        return rand
    }

    private fun generate(chunkX: Int, chunkZ: Int, primer: ChunkPrimer) {
        val STONE = Blocks.BEDROCK.defaultState //Blocks.STONE.defaultState
        val COBBLE = Blocks.COBBLESTONE.defaultState
        val AIR = Blocks.AIR.defaultState

        (0..15).forEach { x ->
            (0..15).forEach { z ->
                (0..60).forEach { y ->
                    primer.setBlockState(x, y, z, STONE)
                }
            }
        }

        /*(0..15).forEach { one ->
            (0..3).forEach {
                (0..4).forEach { y ->
                    primer.setBlockState(one, 60 - y, 6 + it, AIR)
                    primer.setBlockState(6 + it, 60 - y, one, AIR)
                }
            }
            (0..1).forEach {
                primer.setBlockState(one, 55, 7 + it, COBBLE)
                primer.setBlockState(7 + it, 55, one, COBBLE)
            }
        }*/

//        TestStructure.genC1(chunkX, chunkZ, primer)

        if (chunkX in (0..99) && chunkZ in (0..99)) primer.loadStructure(StructureLoader.getStructure(chunkX to chunkZ), 61)

        /*(0..15).forEach { x -> // Cutting off the roof to see the result more easily
            (0..15).forEach { z ->
                (69..70).forEach { y ->
                    primer.setBlockState(x, y, z, AIR)
                }
            }
        }*/
    }

    override fun generateChunk(x: Int, z: Int): Chunk {
        val chunkprimer = ChunkPrimer()

        generate(x, z, chunkprimer)

        val chunk = Chunk(world, chunkprimer, x, z)

        val biomeArray = chunk.biomeArray
        for (i in biomeArray.indices) biomeArray[i] = 42.toByte()

        chunk.generateSkylightMap()
        return chunk
    }

    override fun populate(x: Int, z: Int) {
        // TODO: structures (if need any different from the actual gen)
    }

    override fun generateStructures(chunkIn: Chunk, x: Int, z: Int) = false

    // TODO: add custom mobs
    override fun getPossibleCreatures(creatureType: EnumCreatureType, pos: BlockPos): List<Biome.SpawnListEntry> = emptyList()

    override fun getNearestStructurePos(worldIn: World, structureName: String, position: BlockPos, findUnexplored: Boolean): BlockPos? = null

    override fun recreateStructures(chunkIn: Chunk, x: Int, z: Int) = Unit
}
