import ms.cube.Cube

fun main() {
    val cube = Cube()

    println(cube.solved())

    var count = 0
    do {
        cube.l(3)
        cube.u(1)
        count++
    } while (!cube.solved())
    println(count)
}

