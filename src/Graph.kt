import java.util.*

class Graph<K, N : Graph.Node<K, N>>(private val start: N) {
    companion object {
        fun <K, N : Node<K, N>> withStartNode(start: N) = Graph(start)
    }

    fun shortestPathToOrNull(key: K): Int? {
        val distances = dijkstraFaster()
        return distances[key]
    }

    fun shortestPathTo(key: K): Int {
        val distances = dijkstra { it.key == key }
        return distances[key] ?: error("No path found for $key")
    }

    fun dijkstra(earlyExit: ((N) -> Boolean)? = null): Map<K, Int> {
        val distances = HashMap<K, Int>()
        val queue = PriorityQueue<N>()

        distances[start.key] = start.distance
        queue.add(start)

        while (queue.isNotEmpty()) {
            val current = queue.poll()
            if (current.key in distances && distances[current.key]!! < current.distance) {
                continue
            }
            distances[current.key] = current.distance
            if (earlyExit != null && earlyExit(current)) {
                break
            }
            queue.addAll(
                current.getAdjacentNodes().map { node ->
                    node.apply {
                        distance = current.distance + node.distance
                    }
                })
        }
        return distances
    }

    private fun dijkstraFaster(earlyExit: ((N) -> Boolean)? = null): Map<K, Int> {
        val distances = HashMap<K, Int>()
        val queue = PriorityQueue<N>()
        val visited = mutableSetOf<K>()

        distances[start.key] = start.distance
        queue.add(start)

        while (queue.isNotEmpty()) {
            val current = queue.poll()
            if (current.key in visited) {
                continue
            }
            visited.add(current.key)
            if (earlyExit != null && earlyExit(current)) {
                break
            }
            queue.addAll(current.getAdjacentNodes().filter {
                it.key !in visited && distances[current.key]!! + it.distance < (distances[it.key] ?: Int.MAX_VALUE)
            }.map { node ->
                node.apply {
                    distance = current.distance + node.distance
                }
            }.onEach { node -> distances[node.key] = node.distance })
        }
        return distances
    }

    interface Node<K, N : Node<K, N>> : Comparable<N> {
        var distance: Int
        fun getAdjacentNodes(): List<N>
        override fun compareTo(other: N): Int = distance.compareTo(other.distance)
        val key: K
    }
}