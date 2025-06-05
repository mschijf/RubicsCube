import ms.cube.Cube

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
}

