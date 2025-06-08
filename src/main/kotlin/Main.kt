import ms.cube.Cube
import ms.cube.EdgePattern
import ms.cube.Solver

fun main() {

//    val edgePattern = EdgePattern()
//    edgePattern.preCalculate()

    val cube = Cube.randomCube(1, 13)
    val solver = Solver(cube)
    solver.idaStar()


}

//    val cube = Cube.randomCube(1, 13)
// nodes examined:  44.515.367, bound: 11
// nodes examined:  64.387.050, bound: 11
// nodes examined:  346.404.315, bound: 11

// r1
// l1
// d2
// u1
// r3
// b1
// u1
// l3
// f3
// l1
// u3

