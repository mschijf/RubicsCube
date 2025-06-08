package ms.cube

import java.util.Locale

const val FOUND = -1

class Solver(val root: Cube) {

    private val cornerPattern = CornerPattern()
    private val edgePattern = EdgePattern()
    private val pathStack = ArrayDeque<Pair<String, Cube>>()

    fun idaStar() {
        cornerPattern.read()
        edgePattern.read()

        var bound = root.h()
        println("Start bound: $bound")
        pathStack.add(Pair("--", root))
        while (true) {
            val t = search(bound)
            if (t == FOUND) {
                println("FOUND! $bound")
                break
            }
            if (t >= Int.MAX_VALUE) {
                println("NOT FOUND")
                break
            }
            bound = t
        }
        println( "nodes examined: ${String.format(Locale.GERMANY, " %,d",nodeCount)}, bound: $bound")
        pathStack.forEach { println(it.first) }
    }

    var nodeCount = 0L

    private fun search(bound: Int): Int {
        nodeCount++
        if (nodeCount % 1_000_000 == 0L) {
            println( "nodes examined: ${String.format(Locale.GERMANY, " %,d",nodeCount)}, bound: $bound")
        }

        val (lastMove, node) = pathStack.last()
        val prev = pathStack.getOrNull(pathStack.size - 2)?.first ?: "--"

        val f = pathStack.size - 1 + node.h()
        if (f > bound)
            return f
        if (node.solved())
            return FOUND

        var min = Int.MAX_VALUE
        val successors = node.successorCubeMoves(lastMove, prev ).sortedBy { it.second.h() }
        successors.forEach { successor ->
            if (successor !in pathStack) {
                pathStack.add(successor)
                val t = search(bound)
                if (t == FOUND)
                    return FOUND
                min = minOf (t, min)
                pathStack.removeLast()
            }
        }
        return min
    }


    private fun Cube.h() : Int {
        return maxOf(cornerPattern.getSolveCornerMovesCount(this), edgePattern.getSolveEdgeMovesCount(this))
//        return cornerPattern.getSolveCornerMovesCount(this)
    }
}