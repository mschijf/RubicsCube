package ms.cube


val orientationIndex = Array(6){ Array(6) {Array<Int>(6) {0} } }
val cornerCubieIndex = Array(6){ Array(6) {Array<Int>(6) {0} } }

fun initOrientationIndex() {
    for (i in 0..5) {
        for (j in 0..5) {
            for (k in 0..5) {
                orientationIndex[i][j][k] = -1
                if (i != j && i != k && j != k) {
                    if (i < j && i < k) {
                        orientationIndex[i][j][k] = 0
                    } else if (j < i && j < k) {
                        orientationIndex[i][j][k] = 1
                    } else  {
                        orientationIndex[i][j][k] = 2
                    }
                }
            }
        }
    }
}

fun initCornerCubieIndex() {
    for (i in 0..5) {
        for (j in 0..5) {
            for (k in 0..5) {
                cornerCubieIndex[i][j][k] = -1
                if (i != j && i != k && j != k) {
                    val sortedColors = listOf(i, j, k).sorted()
                    if (sortedColors[0] == startFaceColor[FRONT]) {
                        if (sortedColors[1] == startFaceColor[LEFT] && sortedColors[2] == startFaceColor[UP]) {
                            cornerCubieIndex[i][j][k] = 0
                        } else if (sortedColors[1] == startFaceColor[RIGHT] && sortedColors[2] == startFaceColor[UP]) {
                            cornerCubieIndex[i][j][k] = 1
                        } else if (sortedColors[1] == startFaceColor[LEFT] && sortedColors[2] == startFaceColor[DOWN]) {
                            cornerCubieIndex[i][j][k] = 2
                        } else if (sortedColors[1] == startFaceColor[RIGHT] && sortedColors[2] == startFaceColor[DOWN]) {
                            cornerCubieIndex[i][j][k] = 3
                        }
                    } else if (sortedColors[0] == startFaceColor[BACK]) {
                        if (sortedColors[1] == startFaceColor[LEFT] && sortedColors[2] == startFaceColor[UP]) {
                            cornerCubieIndex[i][j][k] = 4
                        } else if (sortedColors[1] == startFaceColor[RIGHT] && sortedColors[2] == startFaceColor[UP]) {
                            cornerCubieIndex[i][j][k] = 5
                        } else if (sortedColors[1] == startFaceColor[LEFT] && sortedColors[2] == startFaceColor[DOWN]) {
                            cornerCubieIndex[i][j][k] = 6
                        } else if (sortedColors[1] == startFaceColor[RIGHT] && sortedColors[2] == startFaceColor[DOWN]) {
                            cornerCubieIndex[i][j][k] = 7
                        }
                    }
                }
            }
        }
    }
}

fun permutationIndex(permutation: List<Int>): Int {
//    return permutationIndexTable[powerIndex(permutation, 8)]
//
    var index = 0
    var position = 2 // position 1 is paired with factor 0 and so is skipped
    var factor = 1
    for (p in permutation.size - 2 downTo 0) {
        var successors = 0
        for (q in p + 1..<permutation.size) {
            if (permutation[p] > permutation[q]) {
                successors++
            }
        }
        index += (successors * factor)
        factor *= position
        position++
    }
    return index
}

fun powerIndex(list: List<Int>): Int {
    var result = 0
    list.forEach { i ->
        result = result*3 + i
    }
    return result
}
