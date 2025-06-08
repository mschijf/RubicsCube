package ms.cube

import java.io.File
import java.io.InputStream
import java.io.PrintWriter
import java.util.Locale
import kotlin.collections.ArrayDeque

class EdgePattern {

    private val edgePattern = IntArray(42_577_920) {-1}

    private val orientationIndex = initOrientationIndex()
    private val edgeCubieIndex = initEdgeCubieIndex()

    fun getSolveEdgeMovesCount(cube: Cube): Int {
        return edgePattern[cube.getEdgeIndex()]
    }

    private fun initOrientationIndex(): Array<IntArray> {
        val orientationIndex = Array(6){ IntArray(6) {-1} }
        for (i in 0..5) {
            for (j in 0..5) {
                orientationIndex[i][j] = Cube.edgeCubieOrientationIndex(i, j)
            }
        }
        return orientationIndex
    }

    private fun initEdgeCubieIndex(): Array<IntArray> {
        val edgeCubieIndex = Array(6){ IntArray(6) {-1} }
        for (i in 0..5) {
            for (j in 0..5) {
                edgeCubieIndex[i][j] = Cube.edgeCubieIndex(i, j)
            }
        }
        return edgeCubieIndex
    }

    private val onlyEdgeFieldsList = listOf (
        Pair(Cube.FRONT, 7), Pair(Cube.LEFT, 7),
        Pair(Cube.FRONT, 3), Pair(Cube.RIGHT, 7),
        Pair(Cube.FRONT, 1), Pair(Cube.UP, 5),
        Pair(Cube.FRONT, 5), Pair(Cube.DOWN, 5),

        Pair(Cube.BACK, 7), Pair(Cube.LEFT, 3),
        Pair(Cube.BACK, 3), Pair(Cube.RIGHT, 3),
    )

    private fun Cube.getEdgeCubieIndex(): Int {
        val edgeCubieList = listOf(
            edgeCubieIndex[getColor(Cube.FRONT, 7)][getColor(Cube.LEFT, 7)],
            edgeCubieIndex[getColor(Cube.FRONT, 3)][getColor(Cube.RIGHT, 7)],
            edgeCubieIndex[getColor(Cube.FRONT, 1)][getColor(Cube.UP, 5)],
            edgeCubieIndex[getColor(Cube.FRONT, 5)][getColor(Cube.DOWN, 5)],

            edgeCubieIndex[getColor(Cube.BACK, 7)][getColor(Cube.LEFT, 3)],
            edgeCubieIndex[getColor(Cube.BACK, 3)][getColor(Cube.RIGHT, 3)],
        )
        return permutationIndex(edgeCubieList, 12)
    }

    private fun Cube.getEdgeCubieOrientationIndex(): Int {
        val orientationList = listOf(
            orientationIndex[getColor(Cube.FRONT, 7)][getColor(Cube.LEFT, 7)],
            orientationIndex[getColor(Cube.FRONT, 3)][getColor(Cube.RIGHT, 7)],
            orientationIndex[getColor(Cube.FRONT, 1)][getColor(Cube.UP, 5)],
            orientationIndex[getColor(Cube.FRONT, 5)][getColor(Cube.DOWN, 5)],

            orientationIndex[getColor(Cube.BACK, 7)][getColor(Cube.LEFT, 3)],
            orientationIndex[getColor(Cube.BACK, 3)][getColor(Cube.RIGHT, 3)],
        )
        return powerTwoIndex(orientationList)
    }

    private fun Cube.getEdgeIndex(): Int {
        return 64 * this.getEdgeCubieIndex() +       //2^6 = 64
                this.getEdgeCubieOrientationIndex()
    }

    private val permTable = calcPermtable()
    private fun calcPermtable(): Array<IntArray> {
        val permTable = Array(12){ IntArray(12) {1} }
        for (i in 0..11)
            for (j in 0..11) {
                permTable[i][j] = perm(i, j)
            }
        return permTable
    }

    //calculate n! / (n-k)!
    private fun perm(n: Int, k: Int): Int {
        var result = 1
        for (i in n downTo (n - k + 1))
            result *= i
        return result
    }

    private fun permutationIndex(seq: List<Int>, poolSize: Int): Int {
        val pool = (0 until poolSize).toMutableList()
        var index = 0
        val k = seq.size

        for (i in 0 until k) {
            val pos = pool.indexOf(seq[i])
            val remaining = poolSize - i - 1
            index += pos * permTable[remaining][k - i - 1]
            pool.removeAt(pos)
        }

        return index
    }

    private fun powerTwoIndex(list: List<Int>): Int {
        var result = 0
        list.forEach { i ->
            result = result*2 + i
        }
        return result
    }

    fun preCalculate() {
        val startTime = System.currentTimeMillis()
        var max = 0
        var count = 0
        val cube = Cube.initial().onlyFields(onlyEdgeFieldsList)
        val queue = ArrayDeque<Cube>()
        queue.add(cube)
        edgePattern[cube.getEdgeIndex()] = 0
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
            val movesDone = getSolveEdgeMovesCount(current)//cornerPattern[current.getCornerIndex()]
            max = maxOf(max, movesDone)
            count++
            current
                .successorCubes()
                .forEach { cb ->
                    val index = cb.getEdgeIndex()
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
        save()
    }

    private fun save() {
        println("start saving")
        val writer = PrintWriter("data/edgePattern.txt")
        edgePattern.forEach { line ->
            writer.append("$line\n")
        }
        writer.close()
        println("Saving done")
    }

    fun read() {
        println("start reading edge pattern")
        val inputStream: InputStream = File("data/edgePattern.txt").inputStream()
        var index = 0
        inputStream.bufferedReader().forEachLine { line ->
            edgePattern[index] = line.toInt()
            index++
        }
        inputStream.close()
        println("Reading edge pattern done ($index lines read)")
    }
}