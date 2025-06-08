package ms.cube

import java.util.Locale
import kotlin.collections.ArrayDeque

class CornerPattern {

    private val cornerPattern = IntArray(88_179_840) {-1}

    private val orientationIndex = initOrientationIndex()
    private val cornerCubieIndex = initCornerCubieIndex()

    fun Cube.getSolveCornerMovesCount(): Int {
        return cornerPattern[this.getCornerIndex()]
    }

    private fun initOrientationIndex(): Array<Array<IntArray>> {
        val orientationIndex = Array(6){ Array(6) {IntArray(6) {-1} } }
        for (i in 0..5) {
            for (j in 0..5) {
                for (k in 0..5) {
                    try {
                        orientationIndex[i][j][k] = Cube.cornerCubieOrientationIndex(i, j, k)
                    } catch (_: Exception) {
                        orientationIndex[i][j][k] = -1
                    }
                }
            }
        }
        return orientationIndex
    }

    private fun initCornerCubieIndex(): Array<Array<IntArray>> {
        val cornerCubieIndex = Array(6){ Array(6) {IntArray(6) {-1} } }
        for (i in 0..5) {
            for (j in 0..5) {
                for (k in 0..5) {
                    try {
                        cornerCubieIndex[i][j][k] = Cube.cornerCubieIndex(i, j, k)
                    } catch (_: Exception) {
                        cornerCubieIndex[i][j][k] = -1
                    }
                }
            }
        }
        return cornerCubieIndex
    }

    private fun Cube.getCornerCubieIndex(): Int {
        val cornerCubieList = listOf(
            cornerCubieIndex[getColor(Cube.FRONT, 0)][getColor(Cube.LEFT, 0)][getColor(Cube.UP, 6)],
            cornerCubieIndex[getColor(Cube.FRONT, 2)][getColor(Cube.RIGHT, 0)][getColor(Cube.UP, 4)],
            cornerCubieIndex[getColor(Cube.FRONT, 6)][getColor(Cube.LEFT, 6)][getColor(Cube.DOWN, 6)],
            cornerCubieIndex[getColor(Cube.FRONT, 4)][getColor(Cube.RIGHT, 6)][getColor(Cube.DOWN, 4)],

            cornerCubieIndex[getColor(Cube.BACK, 0)][getColor(Cube.LEFT, 2)][getColor(Cube.UP, 0)],
            cornerCubieIndex[getColor(Cube.BACK, 2)][getColor(Cube.RIGHT, 2)][getColor(Cube.UP, 2)],
            cornerCubieIndex[getColor(Cube.BACK, 6)][getColor(Cube.LEFT, 4)][getColor(Cube.DOWN, 0)],
            cornerCubieIndex[getColor(Cube.BACK, 4)][getColor(Cube.RIGHT, 4)][getColor(Cube.DOWN, 2)]
        )
        return permutationIndex(cornerCubieList)
    }

    private fun Cube.getCornerCubieOrientationIndex(): Int {
        val orientationList = listOf(
            orientationIndex[getColor(Cube.FRONT, 0)][getColor(Cube.LEFT, 0)][getColor(Cube.UP, 6)],
            orientationIndex[getColor(Cube.FRONT, 2)][getColor(Cube.RIGHT, 0)][getColor(Cube.UP, 4)],
            orientationIndex[getColor(Cube.FRONT, 6)][getColor(Cube.LEFT, 6)][getColor(Cube.DOWN, 6)],
            orientationIndex[getColor(Cube.FRONT, 4)][getColor(Cube.RIGHT, 6)][getColor(Cube.DOWN, 4)],

            orientationIndex[getColor(Cube.BACK, 0)][getColor(Cube.LEFT, 2)][getColor(Cube.UP, 0)],
            orientationIndex[getColor(Cube.BACK, 2)][getColor(Cube.RIGHT, 2)][getColor(Cube.UP, 2)],
            orientationIndex[getColor(Cube.BACK, 6)][getColor(Cube.LEFT, 4)][getColor(Cube.DOWN, 0)],
//            CubeIndex.orientationIndex[getColor(Cube.BACK, 4)][getColor(Cube.RIGHT, 4)][getColor(Cube.DOWN, 2)]
        )
        return powerIndex(orientationList)
    }

    private fun Cube.getCornerIndex(): Int {

        return 2187 * this.getCornerCubieIndex() +       //3^7 = 2187
                this.getCornerCubieOrientationIndex()
    }

    private fun permutationIndex(permutation: List<Int>): Int {
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

    private fun powerIndex(list: List<Int>): Int {
        var result = 0
        list.forEach { i ->
            result = result*3 + i
        }
        return result
    }

    fun preCalculate() {
        val startTime = System.currentTimeMillis()
        var max = 0
        var count = 0
        val cube = Cube.initial().onlyCornerFields()
        val queue = ArrayDeque<Cube>()
        queue.add(cube)
        cornerPattern[cube.getCornerIndex()] = 0
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
            val movesDone = current.getSolveCornerMovesCount()//cornerPattern[current.getCornerIndex()]
            max = maxOf(max, movesDone)
            count++
            current
                .successorCubes()
                .forEach { cb ->
                    val index = cb.getCornerIndex()
                    if (cornerPattern[index] < 0) {
                        cornerPattern[index] = movesDone + 1
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
        for (i in cornerPattern.indices) {
            if (cornerPattern[i] < 0)
                println(i)
            if (cornerPattern[i] > max)
                max = maxOf(cornerPattern[i], max)
        }
        println("all printed")
        println("max moves (recalculated): $max")
    }
}

//Done in 468.782 sec
//Done in 482.762 sec
//Done in 455.283 sec
//All done in 449.640 sec
//All done in 452.387 sec

//cubes examined:  3.000.000  in pattern:  13.823.140 in queue:  10.823.140 maxMoves: 7 (13.803 sec)  3.912.193 cubes/sec
//cubes examined:  3.000.000  in pattern:  13.823.140 in queue:  10.823.140 maxMoves: 7 (13.464 sec)  4.010.695 cubes/sec

//cubes examined:  3.000.000  in pattern:  13.823.140 in queue:  10.823.140 maxMoves: 7 (13.265 sec)  4.070.863 cubes/sec
//cubes examined:  3.000.000  in pattern:  13.823.140 in queue:  10.823.140 maxMoves: 7 (13.208 sec)  4.088.431 cubes/sec
//cubes examined:  3.000.000  in pattern:  13.823.140 in queue:  10.823.140 maxMoves: 7 (13.139 sec)  4.109.901 cubes/sec