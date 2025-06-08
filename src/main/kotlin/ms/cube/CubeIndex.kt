package ms.cube


object CubeIndex {
    val orientationIndex = Array(6){ Array(6) {Array<Int>(6) {0} } }
    val cornerCubieIndex = Array(6){ Array(6) {Array<Int>(6) {0} } }

    init {
        initCornerCubieIndex()
        initOrientationIndex()
    }

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
                        if (sortedColors[0] == Cube.startFrontColor()) {
                            if (sortedColors[1] == Cube.startLeftColor() && sortedColors[2] == Cube.startUpColor()) {
                                cornerCubieIndex[i][j][k] = 0
                            } else if (sortedColors[1] == Cube.startRightColor() && sortedColors[2] == Cube.startUpColor()) {
                                cornerCubieIndex[i][j][k] = 1
                            } else if (sortedColors[1] == Cube.startLeftColor() && sortedColors[2] == Cube.startDownColor()) {
                                cornerCubieIndex[i][j][k] = 2
                            } else if (sortedColors[1] == Cube.startRightColor() && sortedColors[2] == Cube.startDownColor()) {
                                cornerCubieIndex[i][j][k] = 3
                            }
                        } else if (sortedColors[0] == Cube.startBackColor()) {
                            if (sortedColors[1] == Cube.startLeftColor() && sortedColors[2] == Cube.startUpColor()) {
                                cornerCubieIndex[i][j][k] = 4
                            } else if (sortedColors[1] == Cube.startRightColor() && sortedColors[2] == Cube.startUpColor()) {
                                cornerCubieIndex[i][j][k] = 5
                            } else if (sortedColors[1] == Cube.startLeftColor() && sortedColors[2] == Cube.startDownColor()) {
                                cornerCubieIndex[i][j][k] = 6
                            } else if (sortedColors[1] == Cube.startRightColor() && sortedColors[2] == Cube.startDownColor()) {
                                cornerCubieIndex[i][j][k] = 7
                            }
                        }
                    }
                }
            }
        }
    }
}
