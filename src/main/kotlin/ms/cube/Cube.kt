package ms.cube

/*                        UP.                               UP'
 *                   +---+---+---+                     +---+---+---+
 *                   | 0 | 1 | 2 |                     | 2 | 1 | 0 |
 *                   +---+---+---+                     +---+---+---+
 *                   | 7 |   | 3 |                     | 3 |   | 7 |
 *                   +---+---+---+                     +---+---+---+
 *                   | 6 | 5 | 4 |                     | 2 | 1 | 0 |
 *                   +---+---+---+                     +---+---+---+
 *
 *       LEFT            FRONT            RIGHT            BACK  .          LEFT'
 *  +---+---+---+    +---+---+---+    +---+---+---+    +---+---+---+   +---+---+---+
 *  | 2 | 1 | 0 |    | 0 | 1 | 2 |    | 0 | 1 | 2 |    | 2 | 1 | 0 |   | 2 | 1 | 0 |
 *  +---+---+---+    +---+---+---+    +---+---+---+    +---+---+---+   +---+---+---+
 *  | 3 |   | 7 |    | 7 |   | 3 |    | 7 |   | 3 |    | 3 |   | 7 |   | 3 |   | 7 |
 *  +---+---+---+    +---+---+---+    +---+---+---+    +---+---+---+   +---+---+---+
 *  | 4 | 5 | 6 |    | 6 | 5 | 4 |    | 6 | 5 | 4 |    | 4 | 5 | 6 |   | 4 | 5 | 6 |
 *  +---+---+---+    +---+---+---+    +---+---+---+    +---+---+---+   +---+---+---+
 *
 *                   +---+---+---+                     +---+---+---+
 *                   | 6 | 5 | 4 |                     | 2 | 1 | 0 |
 *                   +---+---+---+                     +---+---+---+
 *                   | 7 |   | 3 |                     | 3 |   | 7 |
 *                   +---+---+---+                     +---+---+---+
 *                   | 0 | 1 | 2 |                     | 4 | 5 | 6 |
 *                   +---+---+---+                     +---+---+---+
 *                       DOWN                              DOWN'
 *
 *
 */



const val WHITE = 0x0
const val ORANGE = 0x1
const val RED = 0x2
const val GREEN = 0x3
const val BLUE = 0x4
const val YELLOW = 0x5

const val FRONT = 0
const val BACK = 1
const val LEFT = 2
const val RIGHT = 3
const val UP = 4
const val DOWN = 5

val startFaceColor = listOf(WHITE, ORANGE, RED, GREEN, BLUE, YELLOW).sorted()
val initial = startFaceColor.map { clr -> (0..7).sumOf {clr.toUInt() shl 4*it} }

data class Cube (
    val bitFaces: List<UInt>) {

    companion object {
        fun initial() = Cube(initial)
    }

    fun d(n: Int = 1): Cube {
        val bitArray: Array<UInt> = Array(6) { face -> bitFaces[face] }
        repeat (n) {
            bitArray.shift(FRONT, 6, RIGHT, 6, BACK, 4, LEFT, 4)
            bitArray.shift(FRONT, 5, RIGHT, 5, BACK, 5, LEFT, 5)
            bitArray.shift(FRONT, 4, RIGHT, 4, BACK, 6, LEFT, 6)
            bitArray[DOWN] = bitArray[DOWN].rotateFace()
        }
        return Cube(bitArray.toList())
    }

    fun u(n: Int = 1): Cube {
        val bitArray: Array<UInt> = Array(6) { face -> bitFaces[face] }
        repeat (n) {
            bitArray.shift(FRONT, 0, RIGHT, 0, BACK, 2, LEFT, 2)
            bitArray.shift(FRONT, 1, RIGHT, 1, BACK, 1, LEFT, 1)
            bitArray.shift(FRONT, 2, RIGHT, 2, BACK, 0, LEFT, 0)
            bitArray[UP] = bitArray[UP].rotateFace()
        }
        return Cube(bitArray.toList())
    }

    fun f(n: Int = 1): Cube {
        val bitArray: Array<UInt> = Array(6) { face -> bitFaces[face] }
        repeat (n) {
            bitArray.shift(UP, 6, LEFT, 6, DOWN, 4, RIGHT, 0)
            bitArray.shift(UP, 5, LEFT, 7, DOWN, 5, RIGHT, 7)
            bitArray.shift(UP, 4, LEFT, 0, DOWN, 6, RIGHT, 6)
            bitArray[FRONT] = bitArray[FRONT].rotateFace()
        }
        return Cube(bitArray.toList())
    }

    fun b(n: Int = 1): Cube {
        val bitArray: Array<UInt> = Array(6) { face -> bitFaces[face] }
        repeat (n) {
            bitArray.shift(UP, 0, LEFT, 4, DOWN, 2, RIGHT, 2)
            bitArray.shift(UP, 1, LEFT, 3, DOWN, 1, RIGHT, 3)
            bitArray.shift(UP, 2, LEFT, 2, DOWN, 0, RIGHT, 4)
            bitArray[BACK] = bitArray[BACK].rotateFace()
        }
        return Cube(bitArray.toList())
    }

    fun l(n: Int = 1): Cube {
        val bitArray: Array<UInt> = Array(6) { face -> bitFaces[face] }
        repeat (n) {
            bitArray.shift(FRONT, 0, DOWN, 6, BACK, 6, UP, 0)
            bitArray.shift(FRONT, 7, DOWN, 7, BACK, 7, UP, 7)
            bitArray.shift(FRONT, 6, DOWN, 0, BACK, 0, UP, 6)
            bitArray[LEFT] = bitArray[LEFT].rotateFace()
        }
        return Cube(bitArray.toList())
    }

    fun r(n: Int = 1): Cube {
        val bitArray: Array<UInt> = Array(6) { face -> bitFaces[face] }
        repeat (n) {
            bitArray.shift(FRONT, 2, DOWN, 4, BACK, 4, UP, 2)
            bitArray.shift(FRONT, 3, DOWN, 3, BACK, 3, UP, 3)
            bitArray.shift(FRONT, 4, DOWN, 2, BACK, 2, UP, 4)
            bitArray[RIGHT] = bitArray[RIGHT].rotateFace()
        }
        return Cube(bitArray.toList())
    }

    fun onlyCornerFields(): Cube {
        return Cube(bitFaces.map { it and 0x0F0F0F0FU })
    }

    fun solved(): Boolean {
        return bitFaces == initial
    }

    fun faceSolved(face: Int): Boolean {
        return bitFaces[face] == initial[face]
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

    override fun toString(): String {
        return "[" + bitFaces.joinToString(", "){it.toString(16)} +" ]"
    }

    fun getColor(face: Int, idx: Int): Int {
        return ((bitFaces[face] and (0xFU shl (4*idx))) shr (4*idx)).toInt()
    }
}