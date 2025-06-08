package ms.cube

import java.util.Locale
import kotlin.collections.ArrayDeque


class CubeCornerSolver {

    private val edgePattern = IntArray(88_179_840) {-1}

    private fun Cube.getCornerCubieIndex(): Int {
        val cornerCubieList = listOf(
            CubeIndex.cornerCubieIndex[getColor(Cube.FRONT, 0)][getColor(Cube.LEFT, 0)][getColor(Cube.UP, 6)],
            CubeIndex.cornerCubieIndex[getColor(Cube.FRONT, 2)][getColor(Cube.RIGHT, 0)][getColor(Cube.UP, 4)],
            CubeIndex.cornerCubieIndex[getColor(Cube.FRONT, 6)][getColor(Cube.LEFT, 6)][getColor(Cube.DOWN, 6)],
            CubeIndex.cornerCubieIndex[getColor(Cube.FRONT, 4)][getColor(Cube.RIGHT, 6)][getColor(Cube.DOWN, 4)],

            CubeIndex.cornerCubieIndex[getColor(Cube.BACK, 0)][getColor(Cube.LEFT, 2)][getColor(Cube.UP, 0)],
            CubeIndex.cornerCubieIndex[getColor(Cube.BACK, 2)][getColor(Cube.RIGHT, 2)][getColor(Cube.UP, 2)],
            CubeIndex.cornerCubieIndex[getColor(Cube.BACK, 6)][getColor(Cube.LEFT, 4)][getColor(Cube.DOWN, 0)],
            CubeIndex.cornerCubieIndex[getColor(Cube.BACK, 4)][getColor(Cube.RIGHT, 4)][getColor(Cube.DOWN, 2)]
        )
        return permutationIndex(cornerCubieList)
    }

    private fun Cube.getCornerCubieOrienationIndex(): Int {
        val orientationList = listOf(
            CubeIndex.orientationIndex[getColor(Cube.FRONT, 0)][getColor(Cube.LEFT, 0)][getColor(Cube.UP, 6)],
            CubeIndex.orientationIndex[getColor(Cube.FRONT, 2)][getColor(Cube.RIGHT, 0)][getColor(Cube.UP, 4)],
            CubeIndex.orientationIndex[getColor(Cube.FRONT, 6)][getColor(Cube.LEFT, 6)][getColor(Cube.DOWN, 6)],
            CubeIndex.orientationIndex[getColor(Cube.FRONT, 4)][getColor(Cube.RIGHT, 6)][getColor(Cube.DOWN, 4)],

            CubeIndex.orientationIndex[getColor(Cube.BACK, 0)][getColor(Cube.LEFT, 2)][getColor(Cube.UP, 0)],
            CubeIndex.orientationIndex[getColor(Cube.BACK, 2)][getColor(Cube.RIGHT, 2)][getColor(Cube.UP, 2)],
            CubeIndex.orientationIndex[getColor(Cube.BACK, 6)][getColor(Cube.LEFT, 4)][getColor(Cube.DOWN, 0)],
//            CubeIndex.orientationIndex[getColor(Cube.BACK, 4)][getColor(Cube.RIGHT, 4)][getColor(Cube.DOWN, 2)]
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
//        return list.reduce { acc, i -> acc * 3 + i } --> slower
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
        println("All done in %d.%03d sec".format(timePassed / 1000, timePassed % 1000))
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
}

//Done in 468.782 sec
//Done in 482.762 sec
//Done in 455.283 sec
//All done in 449.640 sec

//cubes examined:  3.000.000  in pattern:  13.823.140 in queue:  10.823.140 maxMoves: 7 (13.803 sec)  3.912.193 cubes/sec
//cubes examined:  3.000.000  in pattern:  13.823.140 in queue:  10.823.140 maxMoves: 7 (13.464 sec)  4.010.695 cubes/sec

//cubes examined:  3.000.000  in pattern:  13.823.140 in queue:  10.823.140 maxMoves: 7 (13.265 sec)  4.070.863 cubes/sec
//cubes examined:  3.000.000  in pattern:  13.823.140 in queue:  10.823.140 maxMoves: 7 (13.208 sec)  4.088.431 cubes/sec
//cubes examined:  3.000.000  in pattern:  13.823.140 in queue:  10.823.140 maxMoves: 7 (13.139 sec)  4.109.901 cubes/sec