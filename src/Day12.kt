/*import java.util.*

fun main() {
    solve("Day12_test2.txt", 99, 99)
    solve("Day12_test3.txt", 99, 99)
    solve("Day12_test.txt", 1930, 99)
    solve("Day12.txt", 99, 99)
}

private fun solve(path: String, part1: Int, part2: Int) {
    val grid = Grid(lines(path))
    check(part1, part1(grid))
    check(part2, 99)
}

private fun part1(grid: Grid): Int {
    val plantTypeCounter = mutableMapOf<Char, Int>().apply {
        grid.getDistinct().forEach {
            it to 0
        }
    }
    val fences = mutableListOf<Fence>()
    val openVerticalFences = LinkedList<Fence.OpenVertical>()
    val openHorizontalFences = LinkedList<Fence.OpenHorizontal>()

    grid.getAllPoints().forEach { (field, plant) ->
        plantTypeCounter[plant] = plantTypeCounter.getOrPut(plant) { 0 } + 1
        when {
            grid.isCorner(field) -> repeat(2) {
                fences.add(Fence.Vertical(plant, OUTSIDE_PLOT))
                fences.add(Fence.Horizontal(plant, OUTSIDE_PLOT))
            }.also {
                //openVerticalFences.poll().let { fences.add(it.join(plant)) }
                //fences.add(Fence.OpenVertical(plant))
                plant.processOrQueueVerticalFence(openVerticalFences, fences)


                openHorizontalFences.poll().let { fences.add(it.join(plant)) }
                    ?: fences.add(Fence.OpenHorizontal(plant))
            }

            grid.isBorder(field) -> fences.add(Fence(plant, OUTSIDE_PLOT)).also {
                repeat(2) {

                }
            }

            else -> fences.add(Fence(plant, null))
        }
    }
    return 99
}

private fun Plant.processOrQueueVerticalFence(
    openVerticalFences: LinkedList<Fence.OpenVertical>,
    fences: MutableList<Fence>
) {
    if (openVerticalFences.isNotEmpty()) {
        fences.add(Fence.Vertical(openVerticalFences.poll().first, this))
    } else {
        fences.add(Fence.OpenVertical(this))
    }
}

private const val OUTSIDE_PLOT = ' '
private typealias Plant = Char
private typealias RegionId = Pair<Plant, Int>
private typealias Region = Pair<RegionId, List<Point>>

private sealed class Fence(first: Plant, second: Plant?) {
    class OpenVertical(val first: Plant) : Fence(first, null)
    class OpenHorizontal(val first: Plant) : Fence(first, null)
    class Vertical(first: Plant, second: Plant) : Fence(first, second)
    class Horizontal(first: Plant, second: Plant) : Fence(first, second)
}

private fun Fence.OpenVertical.join(second: Plant) = Fence.Vertical(this.first, second)
private fun Fence.OpenHorizontal.join(second: Plant) = Fence.Horizontal(this.first, second)

// check without diagonal
// has 1 adjacent mit same char +1area +3fence
// has 2 adjacent with same char +1area +2fence
// has 3 adjacent with same char +1area +1fence
// has 4 adjacent with same char +1area +0fence
// id of fence can be two points
// points are mapped to char
// amount of area is points with same char
// amount of fences are fence with one char


private fun Grid.findRegionsOf(char: Char): List<Region> {
    return buildList<Region> {
        {
            this@findRegionsOf.findAll(char)
        }
    }
}

private fun Region.calcArea(): Int {
    return 1
}

private fun Region.calcPerimeter(): Int {
    return 1
}*/
