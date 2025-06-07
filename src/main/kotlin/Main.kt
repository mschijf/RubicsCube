import ms.cube.Cube
import ms.cube.CubeCornerSolver
import ms.cube.initCornerCubieIndex
import ms.cube.initOrientationIndex

fun main() {
    val cube = Cube.initial()
    println(cube.solved())

    var count = 0
    var currentCube = cube
    do {
        currentCube = currentCube.l().d()
        count++
    } while (currentCube != cube)
    println(count)

    initOrientationIndex()
    initCornerCubieIndex()

    val startTime = System.currentTimeMillis()

    val solver = CubeCornerSolver()
    solver.solve()

    val timePassed = System.currentTimeMillis() - startTime
    print("Done in %d.%03d sec".format(timePassed / 1000, timePassed % 1000))
    println()
}

