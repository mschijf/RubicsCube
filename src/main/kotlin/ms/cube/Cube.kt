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

private const val cWhite = 0U
private const val cRed = 1U
private const val cGreen = 2U
private const val cBlue = 3U
private const val cYellow = 4U
private const val cOrange = 5U

private val cWhiteFace = (0..7).sumOf {cWhite shl 3*it}
private val cRedFace = (0..7).sumOf {cRed shl 3*it}
private val cGreenFace = (0..7).sumOf {cGreen shl 3*it}
private val cBlueFace = (0..7).sumOf {cBlue shl 3*it}
private val cYellowFace = (0..7).sumOf {cYellow shl 3*it}
private val cOrangeFace = (0..7).sumOf {cOrange shl 3*it}

private val faceMask = (0..7).sumOf {7U shl 3*it}
private val colorFace = listOf(cWhiteFace, cRedFace, cGreenFace, cBlueFace, cYellowFace, cOrangeFace)


class Cube(
    private val bitFaces: List<UInt>) {

    companion object {
        fun initial() : Cube {
            return Cube(colorFace  )
        }
    }


    fun d(n: Int = 1): Cube {
        val bitAr: Array<UInt> = Array(6) { face -> bitFaces[face] }
        repeat (n) {
            bitAr.shift(FRONT, 6, RIGHT, 6, BACK, 4, LEFT, 4)
            bitAr.shift(FRONT, 5, RIGHT, 5, BACK, 5, LEFT, 5)
            bitAr.shift(FRONT, 4, RIGHT, 4, BACK, 6, LEFT, 6)
            bitAr.rotateFace(DOWN)
        }
        return Cube(bitAr.toList())
    }

    fun u(n: Int = 1): Cube {
        val bitAr: Array<UInt> = Array(6) { face -> bitFaces[face] }
        repeat (n) {
            bitAr.shift(FRONT, 0, RIGHT, 0, BACK, 2, LEFT, 2)
            bitAr.shift(FRONT, 1, RIGHT, 1, BACK, 1, LEFT, 1)
            bitAr.shift(FRONT, 2, RIGHT, 2, BACK, 0, LEFT, 0)
            bitAr.rotateFace(UP)
        }
        return Cube(bitAr.toList())
    }

    fun f(n: Int = 1): Cube {
        val bitAr: Array<UInt> = Array(6) { face -> bitFaces[face] }
        repeat (n) {
            bitAr.shift(UP, 6, LEFT, 6, DOWN, 4, RIGHT, 0)
            bitAr.shift(UP, 5, LEFT, 7, DOWN, 5, RIGHT, 7)
            bitAr.shift(UP, 4, LEFT, 0, DOWN, 6, RIGHT, 6)
            bitAr.rotateFace(FRONT)
        }
        return Cube(bitAr.toList())
    }

    fun b(n: Int = 1): Cube {
        val bitAr: Array<UInt> = Array(6) { face -> bitFaces[face] }
        repeat (n) {
            bitAr.shift(UP, 0, LEFT, 4, DOWN, 2, RIGHT, 2)
            bitAr.shift(UP, 1, LEFT, 3, DOWN, 1, RIGHT, 3)
            bitAr.shift(UP, 2, LEFT, 2, DOWN, 0, RIGHT, 4)
            bitAr.rotateFace(BACK)
        }
        return Cube(bitAr.toList())
    }

    fun l(n: Int = 1): Cube {
        val bitAr: Array<UInt> = Array(6) { face -> bitFaces[face] }
        repeat (n) {
            bitAr.shift(FRONT, 0, DOWN, 6, BACK, 6, UP, 0)
            bitAr.shift(FRONT, 7, DOWN, 7, BACK, 7, UP, 7)
            bitAr.shift(FRONT, 6, DOWN, 0, BACK, 0, UP, 6)
            bitAr.rotateFace(LEFT)
        }
        return Cube(bitAr.toList())
    }

    fun r(n: Int = 1): Cube {
        val bitAr: Array<UInt> = Array(6) { face -> bitFaces[face] }
        repeat (n) {
            bitAr.shift(FRONT, 2, DOWN, 4, BACK, 4, UP, 2)
            bitAr.shift(FRONT, 3, DOWN, 3, BACK, 3, UP, 3)
            bitAr.shift(FRONT, 4, DOWN, 2, BACK, 2, UP, 4)
            bitAr.rotateFace(RIGHT)
        }
        return Cube(bitAr.toList())
    }

    fun solved(): Boolean {
        return (0..5).all{face -> faceSolved(face)}
    }

    fun faceSolved(face: Int): Boolean {
        return bitFaces == colorFace
    }

    private fun Array<UInt>.shift(face1: Int, idx1: Int, face2: Int, idx2: Int, face3: Int, idx3: Int, face4: Int, idx4: Int) {
        val bitAr = this
        val colorFacelet1 = (bitAr[face1] and (7U shl 3*idx1)) shr (3*idx1)
        val colorFacelet2 = (bitAr[face2] and (7U shl 3*idx2)) shr (3*idx2)
        val colorFacelet3 = (bitAr[face3] and (7U shl 3*idx3)) shr (3*idx3)
        val colorFacelet4 = (bitAr[face4] and (7U shl 3*idx4)) shr (3*idx4)
        bitAr[face1] = (bitAr[face1] and (7U shl 3*idx1).inv()) or (colorFacelet2 shl 3*idx1)
        bitAr[face2] = (bitAr[face2] and (7U shl 3*idx2).inv()) or (colorFacelet3 shl 3*idx2)
        bitAr[face3] = (bitAr[face3] and (7U shl 3*idx3).inv()) or (colorFacelet4 shl 3*idx3)
        bitAr[face4] = (bitAr[face4] and (7U shl 3*idx4).inv()) or (colorFacelet1 shl 3*idx4)
    }

    private fun Array<UInt>.rotateFace(face: Int) {
        val bitAr = this
        val lastOnes = (bitAr[face] and (63U shl 18)) shr 18
        val shift = bitAr[face] shl 6
        bitAr[face] = (lastOnes or shift) and faceMask
    }
}