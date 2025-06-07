package ms.cube

import java.util.Locale
import kotlin.collections.ArrayDeque


class CubeCornerSolver {

    private fun Cube.getCornerIndex(): Int {
        val cornerCubieList = listOf(
            cornerCubieIndex[this.getColor(FRONT, 0)][this.getColor(LEFT, 0)][this.getColor(UP, 6)],
            cornerCubieIndex[this.getColor(FRONT, 2)][this.getColor(RIGHT, 0)][this.getColor(UP, 4)],
            cornerCubieIndex[this.getColor(FRONT, 6)][this.getColor(LEFT, 6)][this.getColor(DOWN, 6)],
            cornerCubieIndex[this.getColor(FRONT, 4)][this.getColor(RIGHT, 6)][this.getColor(DOWN, 4)],

            cornerCubieIndex[this.getColor(BACK, 0)][this.getColor(LEFT, 2)][this.getColor(UP, 0)],
            cornerCubieIndex[this.getColor(BACK, 2)][this.getColor(RIGHT, 2)][this.getColor(UP, 2)],
            cornerCubieIndex[this.getColor(BACK, 6)][this.getColor(LEFT, 4)][this.getColor(DOWN, 0)],
            cornerCubieIndex[this.getColor(BACK, 4)][this.getColor(RIGHT, 4)][this.getColor(DOWN, 2)]
        )

        val orientationList = listOf(
            orientationIndex[this.getColor(FRONT, 0)][this.getColor(LEFT, 0)][this.getColor(UP, 6)],
            orientationIndex[this.getColor(FRONT, 2)][this.getColor(RIGHT, 0)][this.getColor(UP, 4)],
            orientationIndex[this.getColor(FRONT, 6)][this.getColor(LEFT, 6)][this.getColor(DOWN, 6)],
            orientationIndex[this.getColor(FRONT, 4)][this.getColor(RIGHT, 6)][this.getColor(DOWN, 4)],

            orientationIndex[this.getColor(BACK, 0)][this.getColor(LEFT, 2)][this.getColor(UP, 0)],
            orientationIndex[this.getColor(BACK, 2)][this.getColor(RIGHT, 2)][this.getColor(UP, 2)],
            orientationIndex[this.getColor(BACK, 6)][this.getColor(LEFT, 4)][this.getColor(DOWN, 0)],
//            orientationIndex[this.getColor(BACK, 4)][this.getColor(RIGHT, 4)][this.getColor(DOWN, 2)]
        )

        val cubiePermutationIndex = permutationIndex(cornerCubieList)
        val cubieOrientationIndex = powerIndex(orientationList)

        val pow3_7 = 2187
        return pow3_7 * cubiePermutationIndex + cubieOrientationIndex
    }

    private val edgePattern = IntArray(88_179_840) {-1}

    fun solve() {
        var max = 0
        var count = 0
        val cube = Cube.initial().onlyCornerFields()
        val queue = ArrayDeque<Cube>()
        queue.add(cube)
        edgePattern[cube.getCornerIndex()] = 0
        var inPattern = 1
        while (queue.isNotEmpty()) {
            if (count % 100_000 == 0) {
                println()
                print("queue: ${String.format(Locale.GERMANY, " %,d",queue.size)}" +
                        " maxMoves: $max." +
                        ""
                )

            } else if (count % 1000 == 0) {
                print(".")
            }

            val current = queue.removeFirst()
            val movesDone = edgePattern[current.getCornerIndex()]
            max = maxOf(max, movesDone)
            count++
            current
                .successorCubes()
                .forEach { cb ->
                    val index = cb.getCornerIndex()
                    if (edgePattern[index] < 0) {
                        edgePattern[index] = movesDone + 1
                        inPattern++
                        queue.add(cb)
                    }
                }
        }
        println()
        println("max moves: $max")
        println("Stored in pattern: $inPattern")
        println("start printing errors")
        max = 0
        for (i in edgePattern.indices) {
            if (edgePattern[i] < 0)
                println(i)
            if (edgePattern[i] > max)
                max = maxOf(edgePattern[i], max)
        }
        println("all printed")
        println("max moves (recalculated): $max")
    }

    fun Cube.successorCubes(): List<Cube> {
        return listOf(
            this.l(), this.r(), this.d(), this.u(), this.f(), this.b(),
            this.l(2), this.r(2), this.d(2), this.u(2), this.f(2), this.b(2),
            this.l(3), this.r(3), this.d(3), this.u(3), this.f(3), this.b(3)
        )
    }
}

//Done in 468.782 sec
//Done in 482.762 sec