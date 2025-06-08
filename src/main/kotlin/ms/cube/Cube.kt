package ms.cube

import kotlin.random.Random

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

data class Cube (
    val bitFaces: List<UInt>) {

    companion object {
        const val FRONT = 0
        const val BACK = 1
        const val LEFT = 2
        const val RIGHT = 3
        const val UP = 4
        const val DOWN = 5

        //color-values must be defined in order of: front/down, left/right, up/down
        //             and must lie in 0..5

        private const val COLOR_START_FRONT = 0x0
        private const val COLOR_START_BACK = 0x3
        private const val COLOR_START_LEFT = 0x2
        private const val COLOR_START_RIGHT = 0x1
        private const val COLOR_START_UP = 0x4
        private const val COLOR_START_DOWN = 0x5

        private val initialFaces = initial().bitFaces
        private val colorToInitialFace = initial().bitFaces.mapIndexed{ faceIdx, color -> (color and 0xFU).toInt() to faceIdx }.toMap()

        fun initial() : Cube {
            val faceArray = Array(6){0U}
            faceArray[FRONT] = (0..7).sumOf {COLOR_START_FRONT.toUInt() shl 4*it}
            faceArray[BACK] = (0..7).sumOf {COLOR_START_BACK.toUInt() shl 4*it}
            faceArray[LEFT] = (0..7).sumOf {COLOR_START_LEFT.toUInt() shl 4*it}
            faceArray[RIGHT] = (0..7).sumOf {COLOR_START_RIGHT.toUInt() shl 4*it}
            faceArray[UP] = (0..7).sumOf {COLOR_START_UP.toUInt() shl 4*it}
            faceArray[DOWN] = (0..7).sumOf {COLOR_START_DOWN.toUInt() shl 4*it}
            return Cube(faceArray.toList())
        }

        fun randomCube(seed: Int, randomMoveCount: Int=100) : Cube {
            var cube = initial()
            val random = Random(seed)
            repeat(randomMoveCount) {
                val succ = cube.successorCubes()
                cube = succ[random.nextInt(succ.size)]
            }
            return cube
        }

        fun cornerCubieIndex(color1: Int, color2: Int, color3: Int): Int {
            val sortedColors = listOf(color1, color2, color3).sortedBy { colorToInitialFace[it]!! }
            if (sortedColors[0] == COLOR_START_FRONT) {
                if (sortedColors[1] == COLOR_START_LEFT && sortedColors[2] == COLOR_START_UP) {
                    return 0
                } else if (sortedColors[1] == COLOR_START_RIGHT && sortedColors[2] == COLOR_START_UP) {
                    return 1
                } else if (sortedColors[1] == COLOR_START_LEFT && sortedColors[2] == COLOR_START_DOWN) {
                    return 2
                } else if (sortedColors[1] == COLOR_START_RIGHT && sortedColors[2] == COLOR_START_DOWN) {
                    return 3
                }
            } else if (sortedColors[0] == COLOR_START_BACK) {
                if (sortedColors[1] == COLOR_START_LEFT && sortedColors[2] == COLOR_START_UP) {
                    return 4
                } else if (sortedColors[1] == COLOR_START_RIGHT && sortedColors[2] == COLOR_START_UP) {
                    return 5
                } else if (sortedColors[1] == COLOR_START_LEFT && sortedColors[2] == COLOR_START_DOWN) {
                    return 6
                } else if (sortedColors[1] == COLOR_START_RIGHT && sortedColors[2] == COLOR_START_DOWN) {
                    return 7
                }
            }
            return -1
        }
        
        fun cornerCubieOrientationIndex(color1: Int, color2: Int, color3: Int): Int {
            return if (color1 < color2 && color1 < color3 && color2 != color3) {
                0
            } else if (color2 < color1 && color2 < color3 && color1 != color3) {
                1
            } else if (color3 < color1 && color3 < color2 && color1 != color2) {
                2
            } else {
                -1
            }
        }

        fun edgeCubieOrientationIndex(color1: Int, color2: Int): Int {
            return if (color1 < color2)
                0
            else if (color2 < color1)
                1
            else
                -1
        }

        fun edgeCubieIndex(color1: Int, color2: Int): Int {
            val sortedColors = listOf(color1, color2).sortedBy { colorToInitialFace[it]!! }
            if (sortedColors[0] == COLOR_START_FRONT) {
                if (sortedColors[1] == COLOR_START_LEFT) {
                    return 0
                } else if (sortedColors[1] == COLOR_START_RIGHT) {
                    return 1
                } else if (sortedColors[1] == COLOR_START_UP) {
                    return 2
                } else if (sortedColors[1] == COLOR_START_DOWN) {
                    return 3
                }
            } else if (sortedColors[0] == COLOR_START_BACK) {
                if (sortedColors[1] == COLOR_START_LEFT) {
                    return 4
                } else if (sortedColors[1] == COLOR_START_RIGHT) {
                    return 5
                } else if (sortedColors[1] == COLOR_START_UP) {
                    return 6
                } else if (sortedColors[1] == COLOR_START_DOWN) {
                    return 7
                }
            } else if (sortedColors[0] == COLOR_START_LEFT) {
                if (sortedColors[1] == COLOR_START_UP) {
                    return 8
                } else if (sortedColors[1] == COLOR_START_DOWN) {
                    return 9
                }
            } else if (sortedColors[0] == COLOR_START_RIGHT) {
                if (sortedColors[1] == COLOR_START_UP) {
                    return 10
                } else if (sortedColors[1] == COLOR_START_DOWN) {
                    return 11
                }
            }
            return -1
        }
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

    fun onlyFields(fieldList: List<Pair<Int, Int>>): Cube {
        val bitArray: Array<UInt> = Array(6) { face -> bitFaces[face] }
        for (face in bitFaces.indices) {
            for (fieldIndex in 0..8) {
                val field = Pair(face, fieldIndex)
                if (field in fieldList) {
                    bitArray[face] = bitFaces[face] and (0xFU shl 4*fieldIndex)
                } else {
                    bitArray[face] = bitFaces[face] and (0xFU shl 4*fieldIndex).inv()
                }
            }
        }
        return Cube(bitArray.toList())
    }

    fun solved(): Boolean {
        return bitFaces == initialFaces
    }

    fun faceSolved(face: Int): Boolean {
        return bitFaces[face] == initialFaces[face]
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

    fun successorCubes(): List<Cube> {
        return listOf(
            l(), r(), d(), u(), f(), b(),
            l(2), r(2), d(2), u(2), f(2), b(2),
            l(3), r(3), d(3), u(3), f(3), b(3)
        )
    }

    fun successorCubeMoves(lastMove: String, prevMove: String): List<Pair<String, Cube>> {
        val result = listOf(
            Pair("l1", l()),
            Pair("r1",r()),
            Pair("d1",d()),
            Pair("u1",u()),
            Pair("f1",f()),
            Pair("b1",b()),
            Pair("l2",l(2)),
            Pair("r2",r(2)),
            Pair("d2",d(2)),
            Pair("u2",u(2)),
            Pair("f2",f(2)),
            Pair("b2",b(2)),
            Pair("l3",l(3)),
            Pair("r3",r(3)),
            Pair("d3",d(3)),
            Pair("u3",u(3)),
            Pair("f3",f(3)),
            Pair("b3",b(3))
        ).filterNot { it.first.startsWith(lastMove[0]) }

        return if (lastMove[0] == prevMove.opposite()) {
            result.filterNot { it.first.startsWith(prevMove[0]) }
        } else {
            result
        }

    }

    private val oppositeMap = mapOf (
        'l' to 'r',
        'r' to 'l',
        'u' to 'd',
        'd' to 'u',
        'f' to 'b',
        'b' to 'f',
        '-' to '-'
    )
    fun String.opposite(): Char {
        return oppositeMap[this[0]]!!
    }
}