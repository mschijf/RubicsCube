package ms.cube

/*                        UP
 *                   +---+---+---+
 *                   | 0 | 1 | 2 |
 *                   +---+---+---+
 *                   | 7 |   | 3 |
 *                   +---+---+---+
 *                   | 6 | 5 | 4 |
 *                   +---+---+---+
 *
 *       LEFT            FRONT            RIGHT            BACK
 *  +---+---+---+    +---+---+---+    +---+---+---+    +---+---+---+
 *  | 2 | 1 | 0 |    | 0 | 1 | 2 |    | 0 | 1 | 2 |    | 2 | 1 | 0 |
 *  +---+---+---+    +---+---+---+    +---+---+---+    +---+---+---+
 *  | 3 |   | 7 |    | 7 |   | 3 |    | 7 |   | 3 |    | 3 |   | 7 |
 *  +---+---+---+    +---+---+---+    +---+---+---+    +---+---+---+
 *  | 4 | 5 | 6 |    | 6 | 5 | 4 |    | 6 | 5 | 4 |    | 4 | 5 | 6 |
 *  +---+---+---+    +---+---+---+    +---+---+---+    +---+---+---+
 *
 *                   +---+---+---+
 *                   | 6 | 5 | 4 |
 *                   +---+---+---+
 *                   | 7 |   | 3 |
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

private const val cWhiteFace = 0x00000000U
private const val cRedFace = 0x11111111U
private const val cGreenFace = 0x22222222U
private const val cBlueFace = 0x33333333U
private const val cYellowFace = 0x44444444U
private const val cOrangeFace = 0x55555555U

private const val cWhite = 0U
private const val cRed = 1U
private const val cGreen = 2U
private const val cBlue = 3U
private const val cYellow = 4U
private const val cOrange = 5U

private val colorFace = listOf(cWhiteFace, cRedFace, cGreenFace, cBlueFace, cYellowFace, cOrangeFace)


data class Cube private constructor (
    private val bitFaces: List<UInt>) {

    companion object {
        fun initial() = Cube(colorFace)
    }

    fun d(n: Int = 1): Cube {
        val bitAr: Array<UInt> = Array(6) { face -> bitFaces[face] }
        repeat (n) {
            bitAr.shift(FRONT, 6, RIGHT, 6, BACK, 4, LEFT, 4)
            bitAr.shift(FRONT, 5, RIGHT, 5, BACK, 5, LEFT, 5)
            bitAr.shift(FRONT, 4, RIGHT, 4, BACK, 6, LEFT, 6)
            bitAr[DOWN] = bitAr[DOWN].rotateFace()
        }
        return Cube(bitAr.toList())
    }

    fun u(n: Int = 1): Cube {
        val bitAr: Array<UInt> = Array(6) { face -> bitFaces[face] }
        repeat (n) {
            bitAr.shift(FRONT, 0, RIGHT, 0, BACK, 2, LEFT, 2)
            bitAr.shift(FRONT, 1, RIGHT, 1, BACK, 1, LEFT, 1)
            bitAr.shift(FRONT, 2, RIGHT, 2, BACK, 0, LEFT, 0)
            bitAr[UP] = bitAr[UP].rotateFace()
        }
        return Cube(bitAr.toList())
    }

    fun f(n: Int = 1): Cube {
        val bitAr: Array<UInt> = Array(6) { face -> bitFaces[face] }
        repeat (n) {
            bitAr.shift(UP, 6, LEFT, 6, DOWN, 4, RIGHT, 0)
            bitAr.shift(UP, 5, LEFT, 7, DOWN, 5, RIGHT, 7)
            bitAr.shift(UP, 4, LEFT, 0, DOWN, 6, RIGHT, 6)
            bitAr[FRONT] = bitAr[FRONT].rotateFace()
        }
        return Cube(bitAr.toList())
    }

    fun b(n: Int = 1): Cube {
        val bitAr: Array<UInt> = Array(6) { face -> bitFaces[face] }
        repeat (n) {
            bitAr.shift(UP, 0, LEFT, 4, DOWN, 2, RIGHT, 2)
            bitAr.shift(UP, 1, LEFT, 3, DOWN, 1, RIGHT, 3)
            bitAr.shift(UP, 2, LEFT, 2, DOWN, 0, RIGHT, 4)
            bitAr[BACK] = bitAr[BACK].rotateFace()
        }
        return Cube(bitAr.toList())
    }

    fun l(n: Int = 1): Cube {
        val bitAr: Array<UInt> = Array(6) { face -> bitFaces[face] }
        repeat (n) {
            bitAr.shift(FRONT, 0, DOWN, 6, BACK, 6, UP, 0)
            bitAr.shift(FRONT, 7, DOWN, 7, BACK, 7, UP, 7)
            bitAr.shift(FRONT, 6, DOWN, 0, BACK, 0, UP, 6)
            bitAr[LEFT] = bitAr[LEFT].rotateFace()
        }
        return Cube(bitAr.toList())
    }

    fun r(n: Int = 1): Cube {
        val bitAr: Array<UInt> = Array(6) { face -> bitFaces[face] }
        repeat (n) {
            bitAr.shift(FRONT, 2, DOWN, 4, BACK, 4, UP, 2)
            bitAr.shift(FRONT, 3, DOWN, 3, BACK, 3, UP, 3)
            bitAr.shift(FRONT, 4, DOWN, 2, BACK, 2, UP, 4)
            bitAr[RIGHT] = bitAr[RIGHT].rotateFace()
        }
        return Cube(bitAr.toList())
    }

    fun solved(): Boolean {
        return bitFaces == colorFace
    }

    fun faceSolved(face: Int): Boolean {
        return bitFaces[face] == colorFace[face]
    }

    private fun Array<UInt>.shift(face1: Int, idx1: Int, face2: Int, idx2: Int, face3: Int, idx3: Int, face4: Int, idx4: Int) {
        val bitArray = this
        val clrFacelet1 = (bitArray[face1] and (0xFU shl 4*idx1)) shr (4*idx1)
        val clrFacelet2 = (bitArray[face2] and (0xFU shl 4*idx2)) shr (4*idx2)
        val clrFacelet3 = (bitArray[face3] and (0xFU shl 4*idx3)) shr (4*idx3)
        val clrFacelet4 = (bitArray[face4] and (0xFU shl 4*idx4)) shr (4*idx4)
        bitArray[face1] = (bitArray[face1] and (0xFU shl 4*idx1).inv()) or (clrFacelet2 shl 4*idx1)
        bitArray[face2] = (bitArray[face2] and (0xFU shl 4*idx2).inv()) or (clrFacelet3 shl 4*idx2)
        bitArray[face3] = (bitArray[face3] and (0xFU shl 4*idx3).inv()) or (clrFacelet4 shl 4*idx3)
        bitArray[face4] = (bitArray[face4] and (0xFU shl 4*idx4).inv()) or (clrFacelet1 shl 4*idx4)
    }

    private fun UInt.rotateFace(): UInt {
        val lastOnes = (this and (0xFFU shl 24)) shr 24
        val shift = this shl 8
        return (lastOnes or shift)
    }
}