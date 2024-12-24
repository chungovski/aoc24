fun main() {
    solve("Day23_test.txt", 7, "co,de,ka,ta")
    solve("Day23.txt", 926, "az,ed,hz,it,ld,nh,pc,td,ty,ux,wc,yg,zz")
}

private fun solve(path: String, part1: Int, part2: String) {
    val pairs = lines(path).map {
        it.split('-').run { this[0] to this[1] }
    }
    check(part1, part1(pairs))
    check(part2, part2(pairs))
}

private typealias Connection = Pair<String, String>
private typealias Clusters = Map<String, Set<String>>
private typealias Clique = Set<String>

private fun part1(connections: List<Connection>): Int = connections.clusters()
    .cliques().count { it.any { str -> str.startsWith('t') } }

private fun part2(connections: List<Connection>): String =
    BronKerbosch(connections.clusters()).largestClique()
        .sorted().joinToString(",")

private fun List<Connection>.clusters(): Clusters = buildMap<String, MutableSet<String>> {
    this@clusters.forEach { (a, b) ->
        computeIfAbsent(a) { mutableSetOf() }.add(b)
        computeIfAbsent(b) { mutableSetOf() }.add(a)
    }
}

private fun Map<String, Set<String>>.cliques(): Set<Clique> = buildSet {
    this@cliques.forEach { (key, values) ->
        values.forEach { firstVal ->
            values.filter {
                it != firstVal &&
                    this@cliques[it]?.contains(firstVal) == true
            }.forEach { secondVal ->
                add(setOf(key, firstVal, secondVal))
            }
        }
    }
}