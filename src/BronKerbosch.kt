// Ripped from https://github.com/tginsberg/advent-2018-kotlin/blob/master/src/main/kotlin/com/ginsberg/advent2018/BronKerbosch.kt
class BronKerbosch<T>(private val neighbors: Map<T, Set<T>>) {
    private var bestR: Set<T> = emptySet()

    fun largestClique(): Set<T> {
        execute(neighbors.keys)
        return bestR
    }

    private fun execute(p: Set<T>, r: Set<T> = emptySet(), x: Set<T> = emptySet()) {
        // Potential best R value will be compared to the best so far
        if (p.isEmpty() && x.isEmpty() && r.size > bestR.size) {
            bestR = r
        } else {
            (p + x).maxByOrNull { neighbors.getValue(it).size }?.let { pivot ->
                p.minus(neighbors[pivot]!!).forEach { candidate ->
                    neighbors[candidate]?.let { neighbors ->
                        execute(p.intersect(neighbors), r + candidate, x.intersect(neighbors))
                    }
                }
            }
        }
    }
}