import ms.cube.Cube

fun main() {
    val cube = Cube()

    println(cube.solved())

    var count = 0
    do {
        cube.l(1)
        cube.r(1)
        cube.u(1)
        cube.d(1)
        count++
    } while (!cube.solved())
    println(count)
}

