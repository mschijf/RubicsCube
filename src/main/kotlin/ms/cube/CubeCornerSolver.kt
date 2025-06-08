package ms.cube

import java.util.Locale
import kotlin.collections.ArrayDeque


class CubeCornerSolver {

    private val edgePattern = IntArray(88_179_840) {-1}

    private fun Cube.getCornerCubieIndex(): Int {
        val cornerCubieList = listOf(
            CubeIndex.cornerCubieIndex[this.getColor(FRONT, 0)][this.getColor(LEFT, 0)][this.getColor(UP, 6)],
            CubeIndex.cornerCubieIndex[this.getColor(FRONT, 2)][this.getColor(RIGHT, 0)][this.getColor(UP, 4)],
            CubeIndex.cornerCubieIndex[this.getColor(FRONT, 6)][this.getColor(LEFT, 6)][this.getColor(DOWN, 6)],
            CubeIndex.cornerCubieIndex[this.getColor(FRONT, 4)][this.getColor(RIGHT, 6)][this.getColor(DOWN, 4)],

            CubeIndex.cornerCubieIndex[this.getColor(BACK, 0)][this.getColor(LEFT, 2)][this.getColor(UP, 0)],
            CubeIndex.cornerCubieIndex[this.getColor(BACK, 2)][this.getColor(RIGHT, 2)][this.getColor(UP, 2)],
            CubeIndex.cornerCubieIndex[this.getColor(BACK, 6)][this.getColor(LEFT, 4)][this.getColor(DOWN, 0)],
            CubeIndex.cornerCubieIndex[this.getColor(BACK, 4)][this.getColor(RIGHT, 4)][this.getColor(DOWN, 2)]
        )
        return permutationIndex(cornerCubieList)
    }

    private fun Cube.getCornerCubieOrienationIndex(): Int {
        val orientationList = listOf(
            CubeIndex.orientationIndex[this.getColor(FRONT, 0)][this.getColor(LEFT, 0)][this.getColor(UP, 6)],
            CubeIndex.orientationIndex[this.getColor(FRONT, 2)][this.getColor(RIGHT, 0)][this.getColor(UP, 4)],
            CubeIndex.orientationIndex[this.getColor(FRONT, 6)][this.getColor(LEFT, 6)][this.getColor(DOWN, 6)],
            CubeIndex.orientationIndex[this.getColor(FRONT, 4)][this.getColor(RIGHT, 6)][this.getColor(DOWN, 4)],

            CubeIndex.orientationIndex[this.getColor(BACK, 0)][this.getColor(LEFT, 2)][this.getColor(UP, 0)],
            CubeIndex.orientationIndex[this.getColor(BACK, 2)][this.getColor(RIGHT, 2)][this.getColor(UP, 2)],
            CubeIndex.orientationIndex[this.getColor(BACK, 6)][this.getColor(LEFT, 4)][this.getColor(DOWN, 0)],
//            CubeIndex.orientationIndex[this.getColor(BACK, 4)][this.getColor(RIGHT, 4)][this.getColor(DOWN, 2)]
        )
        return powerIndex(orientationList)
    }

    private fun Cube.getCornerIndex(): Int {
        val pow3_7 = 2187
        return pow3_7 * this.getCornerCubieIndex() + this.getCornerCubieOrienationIndex()
    }

    fun permutationIndex(permutation: List<Int>): Int {
        var index = 0
        var position = 2 // position 1 is paired with factor 0 and so is skipped
        var factor = 1
        for (p in permutation.size - 2 downTo 0) {
            var successors = 0
            for (q in p + 1..<permutation.size) {
                if (permutation[p] > permutation[q]) {
                    successors++
                }
            }
            index += (successors * factor)
            factor *= position
            position++
        }
        return index
    }

    fun powerIndex(list: List<Int>): Int {
        var result = 0
        list.forEach { i ->
            result = result*3 + i
        }
        return result
    }



    fun solve() {
        val startTime = System.currentTimeMillis()
        var max = 0
        var count = 0
        val cube = Cube.initial().onlyCornerFields()
        val queue = ArrayDeque<Cube>()
        queue.add(cube)
        edgePattern[cube.getCornerIndex()] = 0
        var inPattern = 1
        while (queue.isNotEmpty()) {
            if (count % 100_000 == 0) {
                val timePassed = System.currentTimeMillis() - startTime
                println()
                print("" +
                        "cubes examined: ${String.format(Locale.GERMANY, " %,d",count)} " +
                        " in pattern: ${String.format(Locale.GERMANY, " %,d",inPattern)}" +
                        " in queue: ${String.format(Locale.GERMANY, " %,d",queue.size)}" +
                        " maxMoves: $max" +
                        " (%d.%03d sec)".format(timePassed / 1000, timePassed % 1000) +
                        " ${String.format(Locale.GERMANY, " %,d",(1000.0*count*18.0 / timePassed).toInt())} cubes/sec" +
                        ""
                )
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
        val timePassed = System.currentTimeMillis() - startTime
        println()
        println()
        print("All done in %d.%03d sec".format(timePassed / 1000, timePassed % 1000))
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
//Done in 455.283 sec