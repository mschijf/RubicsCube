package ms.tools

//calculate n! / (n-k)!
fun perm(n: Int, k: Int): Int {
    var result = 1
    for (i in n downTo (n - k + 1))
        result *= i
    return result
}

fun permutationIndex(seq: List<Int>, poolSize: Int): Int {
    val pool = (0 until poolSize).toMutableList()
    var index = 0
    val k = seq.size

    for (i in 0 until k) {
        val pos = pool.indexOf(seq[i])
        val remaining = poolSize - i - 1
        index += pos * perm(remaining, k - i - 1)
        pool.removeAt(pos)
    }

    return index
}

fun powerTwoIndex(list: List<Int>): Int {
    var result = 0
    list.forEach { i ->
        result = result*2 + i
    }
    return result
}

fun permutationIndex(permutation: List<Int>): Int {
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

fun powerThreeIndex(list: List<Int>): Int {
    var result = 0
    list.forEach { i ->
        result = result*3 + i
    }
    return result
}
