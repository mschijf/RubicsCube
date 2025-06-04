import ms.cube.Cube

fun main() {
    val cube = Cube()

    println(cube.solved())

    var count = 0
    do {
        cube.rotateRightFace()
        cube.rotateUpFace()
        count++
    } while (!cube.solved())
    println(count)
}

