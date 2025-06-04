package ms.cube

private const val DOWN = 0
private const val UP = 1
private const val LEFT = 2
private const val RIGHT = 3
private const val FRONT = 4
private const val BACK = 5

private const val cWhite: ULong = 0u
private const val cRed: ULong = 1u
private const val cGreen: ULong = 2u
private const val cBlue: ULong = 3u
private const val cYellow: ULong = 4u
private const val cOrange: ULong = 5u

class Cube {
    private val ar: Array<Array<Color>> = Array(6) { Array(9){ Color.WHITE} }
    private val bitAr: Array<ULong> = Array(6) { cWhite }

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

    fun rotateDownFace() {
        shift(FRONT, 6, RIGHT, 6, BACK, 4, LEFT, 4)
        shift(FRONT, 5, RIGHT, 5, BACK, 5, LEFT, 5)
        shift(FRONT, 4, RIGHT, 4, BACK, 6, LEFT, 6)
    }

    fun rotateUpFace() {
        shift(FRONT, 0, RIGHT, 0, BACK, 2, LEFT, 2)
        shift(FRONT, 1, RIGHT, 1, BACK, 1, LEFT, 1)
        shift(FRONT, 2, RIGHT, 2, BACK, 0, LEFT, 0)
    }

    fun rotateFrontFace() {
        shift(UP, 6, LEFT, 6, DOWN, 4, RIGHT, 0)
        shift(UP, 5, LEFT, 7, DOWN, 5, RIGHT, 7)
        shift(UP, 4, LEFT, 0, DOWN, 6, RIGHT, 6)
    }

    fun rotateBackFace() {
        shift(UP, 0, LEFT, 4, DOWN, 2, RIGHT, 2)
        shift(UP, 1, LEFT, 3, DOWN, 1, RIGHT, 3)
        shift(UP, 2, LEFT, 2, DOWN, 0, RIGHT, 4)
    }

    fun rotateLeftFace() {
        shift(FRONT, 0, DOWN, 6, BACK, 6, UP, 0)
        shift(FRONT, 7, DOWN, 7, BACK, 7, UP, 7)
        shift(FRONT, 6, DOWN, 0, BACK, 0, UP, 6)
    }

    fun rotateRightFace() {
        shift(FRONT, 2, DOWN, 4, BACK, 4, UP, 2)
        shift(FRONT, 3, DOWN, 3, BACK, 3, UP, 3)
        shift(FRONT, 4, DOWN, 2, BACK, 2, UP, 4)
    }


    private fun shift(face1: Int, location1: Int, face2: Int, location2: Int, face3: Int, location3: Int, face4: Int, location4: Int) {
        val tmp = ar[face1][location1]
        ar[face1][location1]=ar[face2][location2]
        ar[face2][location2]=ar[face3][location3]
        ar[face3][location3]=ar[face4][location4]
        ar[face4][location4] = tmp
    }

    private fun rotateFace(face: Int) {
        val tmp = ar[face][0]
        for (i in 0 .. 6) {
            ar[face][i]=ar[face][(i+6) % 8]
        }
        ar[face][7]=tmp
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