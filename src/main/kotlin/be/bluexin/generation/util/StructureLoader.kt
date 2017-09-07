package be.bluexin.generation.util

import be.bluexin.generation.*
import be.bluexin.generation.util.StructureLoader.Structures.*
import com.teamwizardry.librarianlib.features.structure.Structure
import net.minecraft.util.ResourceLocation
import java.util.*
import kotlin.system.measureTimeMillis


/**
 * Part of generationtests by Bluexin, released under GNU GPLv3.
 *
 * @author Bluexin
 */
object StructureLoader {

    private lateinit var grid: Grid

    fun init(rng: Random) {
        grid = Grid(100, 100, rng)
        grid += Room(0 to 0)
        val time = measureTimeMillis {
            WGraph(grid.generate())
                    .explore()
                    .makePaths()
                    .reset()
                    .explore()
                    .cleanGrid()
        }
        println("Took $time millis to generate the grid.")
    }

    fun getStructure(position: Position) = grid[position].structure

    private enum class Structures(resource: String) {
        CORRIDOR_STRAIGHT("labyrinth/corridor_straight_16x"), // NS
        CORRIDOR_T("labyrinth/corridor_t_16x"), // NWS
        CORRIDOR_L("labyrinth/corridor_l_16x"), // NW
        CORRIDOR_PLUS("labyrinth/corridor_plus_16x"),
        CORRIDOR_DEAD_END("labyrinth/corridor_dead_end_16x"), // N
        ROOM_STRAIGHT("labyrinth/room_straight_16x"), // NS
        ROOM_T("labyrinth/room_t_16x"), // NES
        ROOM_L("labyrinth/room_l_16x"), // NE
        ROOM_PLUS("labyrinth/room_plus_16x"),
        ROOM_DEAD_END("labyrinth/room_dead_end_16x"), // N
        WALL("labyrinth/wall_16x");

        val struct = Structure(ResourceLocation(GenerationTests.MODID, resource))
    }

    private val Tile.structure
        get() = when (this) {
            is Room -> when (this.connections.size) {
                1 -> ROOM_DEAD_END.struct to this.connections.first()
                2 -> {
                    val arr = this.connections.toTypedArray()
                    if (!arr[0] == arr[1]) ROOM_STRAIGHT.struct to if (Orientation.N in this.connections) Orientation.N else Orientation.E
                    else ROOM_L.struct to (arr.maxBy { (it.ordinal + 1 - arr.map { it.ordinal }.min()!!) % 4 }!! shl 1)
                }
                3 -> ROOM_T.struct to (Orientation.values().filter { it !in this.connections }.first() shr 1)
                4 -> ROOM_PLUS.struct to Orientation.N
                else -> WALL.struct to Orientation.N
            }
            is Corridor -> when (this.connections.size) {
                1 -> CORRIDOR_DEAD_END.struct to this.connections.first()
                2 -> {
                    val arr = this.connections.toTypedArray()
                    if (!arr[0] == arr[1]) CORRIDOR_STRAIGHT.struct to if (Orientation.N in this.connections) Orientation.N else Orientation.E
                    else CORRIDOR_L.struct to (arr.maxBy { (it.ordinal + 1 - arr.map { it.ordinal }.min()!!) % 4 }!! shl 1)
                }
                3 -> CORRIDOR_T.struct to (Orientation.values().filter { it !in this.connections }.first() shl 1)
                4 -> CORRIDOR_PLUS.struct to Orientation.N
                else -> WALL.struct to Orientation.N
            }
            else -> WALL.struct to Orientation.N
        }

    fun printGrid() = println(grid)
}
