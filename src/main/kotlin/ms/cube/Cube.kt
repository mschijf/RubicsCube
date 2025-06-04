package ms.cube

/*                        UP
 *                   +---+---+---+
 *                   | 0 | 1 | 2 |
 *                   +---+---+---+
 *                   | 7 | 8 | 3 |
 *                   +---+---+---+
 *                   | 6 | 5 | 4 |
 *                   +---+---+---+
 *
 *       LEFT            FRONT            RIGHT            BACK
 *  +---+---+---+    +---+---+---+    +---+---+---+    +---+---+---+
 *  | 2 | 1 | 0 |    | 0 | 1 | 2 |    | 0 | 1 | 2 |    | 2 | 1 | 0 |
 *  +---+---+---+    +---+---+---+    +---+---+---+    +---+---+---+
 *  | 3 | 8 | 7 |    | 7 | 8 | 3 |    | 7 | 8 | 3 |    | 3 | 8 | 7 |
 *  +---+---+---+    +---+---+---+    +---+---+---+    +---+---+---+
 *  | 4 | 5 | 6 |    | 6 | 5 | 4 |    | 6 | 5 | 4 |    | 4 | 5 | 6 |
 *  +---+---+---+    +---+---+---+    +---+---+---+    +---+---+---+
 *
 *                   +---+---+---+
 *                   | 6 | 5 | 4 |
 *                   +---+---+---+
 *                   | 7 | 8 | 3 |
 *                   +---+---+---+
 *                   | 0 | 1 | 2 |
 *                   +---+---+---+
 *                       DOWN
 *
 *
 */


private const val DOWN = 0
private const val UP = 1
private const val LEFT = 2
private const val RIGHT = 3
private const val FRONT = 4
private const val BACK = 5

class Cube {
    private val ar: Array<Array<Color>> = Array(6) { Array(9){ Color.WHITE} }

    init {
        init()
    }

    fun init() {
        (0..8).forEach { ar[DOWN][it] = Color.WHITE }
        (0..8).forEach { ar[UP][it] = Color.RED }
        (0..8).forEach { ar[LEFT][it] = Color.GREEN }
        (0..8).forEach { ar[RIGHT][it] = Color.BLUE }
        (0..8).forEach { ar[FRONT][it] = Color.YELLOW }
        (0..8).forEach { ar[BACK][it] = Color.ORANGE }
    }

    fun d(n: Int = 1) {
        repeat (n) {
            shift(FRONT, 6, RIGHT, 6, BACK, 4, LEFT, 4)
            shift(FRONT, 5, RIGHT, 5, BACK, 5, LEFT, 5)
            shift(FRONT, 4, RIGHT, 4, BACK, 6, LEFT, 6)
        }
        rotateFace(DOWN)
    }

    fun u(n: Int = 1) {
        repeat (n) {
            shift(FRONT, 0, RIGHT, 0, BACK, 2, LEFT, 2)
            shift(FRONT, 1, RIGHT, 1, BACK, 1, LEFT, 1)
            shift(FRONT, 2, RIGHT, 2, BACK, 0, LEFT, 0)
        }
        rotateFace(UP)
    }

    fun f(n: Int = 1) {
        repeat (n) {
            shift(UP, 6, LEFT, 6, DOWN, 4, RIGHT, 0)
            shift(UP, 5, LEFT, 7, DOWN, 5, RIGHT, 7)
            shift(UP, 4, LEFT, 0, DOWN, 6, RIGHT, 6)
        }
        rotateFace(FRONT)
    }

    fun b(n: Int = 1) {
        repeat (n) {
            shift(UP, 0, LEFT, 4, DOWN, 2, RIGHT, 2)
            shift(UP, 1, LEFT, 3, DOWN, 1, RIGHT, 3)
            shift(UP, 2, LEFT, 2, DOWN, 0, RIGHT, 4)
        }
        rotateFace(BACK)
    }

    fun l(n: Int = 1) {
        repeat (n) {
            shift(FRONT, 0, DOWN, 6, BACK, 6, UP, 0)
            shift(FRONT, 7, DOWN, 7, BACK, 7, UP, 7)
            shift(FRONT, 6, DOWN, 0, BACK, 0, UP, 6)
        }
        rotateFace(LEFT)
    }

    fun r(n: Int = 1) {
        repeat (n) {
            shift(FRONT, 2, DOWN, 4, BACK, 4, UP, 2)
            shift(FRONT, 3, DOWN, 3, BACK, 3, UP, 3)
            shift(FRONT, 4, DOWN, 2, BACK, 2, UP, 4)
        }
        rotateFace(RIGHT)
    }


    private fun shift(face1: Int, idx1: Int, face2: Int, idx2: Int, face3: Int, idx3: Int, face4: Int, idx4: Int) {
        val tmp = ar[face1][idx1]
        ar[face1][idx1]=ar[face2][idx2]
        ar[face2][idx2]=ar[face3][idx3]
        ar[face3][idx3]=ar[face4][idx4]
        ar[face4][idx4] = tmp
    }

    private fun rotateFace(face: Int) {
        val tmp = ar[face][7]
        ar[face][7]=ar[face][5]
        ar[face][5]=ar[face][3]
        ar[face][3]=ar[face][1]
        ar[face][1]=tmp

        val tmp2 = ar[face][6]
        ar[face][6]=ar[face][4]
        ar[face][4]=ar[face][2]
        ar[face][2]=ar[face][0]
        ar[face][0]=tmp2
    }

    fun faceSolved(face: Int): Boolean {
        return (0..8).all{ar[face][it]==ar[face][8]}
    }

    fun solved(): Boolean {
        return (0..5).all{face -> faceSolved(face)}
    }
}

enum class Color {
    WHITE, RED, GREEN, BLUE, YELLOW, ORANGE
}