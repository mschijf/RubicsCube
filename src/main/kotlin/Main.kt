import ms.cube.Cube

fun main() {
    val cube = Cube.initial()

    val c = cube.copy()
    println(cube.solved())

    var count = 0
    var currentCube = cube
    do {
        currentCube = currentCube.l().d()
        count++
    } while (currentCube != cube)
    println(count)


    val startTime = System.currentTimeMillis()
    println("start calculating")
    repeat(1000_000_000) {
        currentCube = currentCube.l()
    }
    val timePassed = System.currentTimeMillis() - startTime
    print("Done in %d.%03d sec".format(timePassed / 1000, timePassed % 1000))
    println()

}

