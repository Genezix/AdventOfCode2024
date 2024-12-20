package common

import adventofcode2024.day6.ProgramDay6

enum class Heading4 {
    N, E, S, W;

    fun rotateRight() = when (this) {
        N -> E
        E -> S
        S -> W
        W -> N
    }

    fun rotateLeft() = when (this) {
        N -> W
        E -> N
        S -> E
        W -> S
    }

    fun toArrow() = when(this) {
        N -> '^'
        E -> '>'
        S -> 'v'
        W -> '<'
    }
}

enum class Heading8 {
    N, NE, E, ES, S, SW, W, WN;

    fun rotateRight() = when (this) {
        N -> NE
        NE -> E
        E -> ES
        ES -> S
        S -> SW
        SW -> W
        W -> WN
        WN -> N
    }

    fun rotateLeft() = when (this) {
        N -> WN
        NE -> N
        E -> NE
        ES -> E
        S -> ES
        SW -> S
        W -> SW
        WN -> W
    }
}

data class Position2D(var x: Int, var y: Int, var value: Char = '.', var score: Long? = null) {
    val neighbors: MutableList<Position2D> = mutableListOf()

    fun getNeighbor(heading4: Heading4) = when (heading4) {
        Heading4.N -> neighbors.firstOrNull { it.x == this.x && it.y == this.y - 1 }
        Heading4.E -> neighbors.firstOrNull { it.x == this.x + 1 && it.y == this.y }
        Heading4.S -> neighbors.firstOrNull { it.x == this.x && it.y == this.y + 1 }
        Heading4.W -> neighbors.firstOrNull { it.x == this.x - 1 && it.y == this.y }
    }

    fun calculateScoreAndGetNextPosition(
        target: Position2D,
        obstacles: List<Char> = listOf('#'),
        frees: List<Char> = listOf('.'),
    ) : List<Position2D>{
        return this.neighbors.filter { !obstacles.contains(it.value) }.mapNotNull { neighbor ->
            neighbor.getNextPositions(target, score!! + 1L, frees)
        }
    }

    private fun getNextPositions(
        target: Position2D,
        score: Long,
        frees: List<Char>,
    ) : Position2D? {
        if (target.score != null && target.score!! < score) return null

        val nextPosition = this
            .takeIf { (frees.contains(it.value) || it == target) }
            ?.takeIf { it.score == null || (it.score!! > score) }

        if (target == nextPosition) {
            if (target.score == null || target.score!! > score) {
                target.score = score
            }
        } else {
            return nextPosition?.also { if (it.score == null || it.score!! > score) it.score = score }
        }
        return null
    }

    fun calculateScoreOnMove(
        target: Position2D,
        score: Long,
        obstacles: List<Char> = listOf('#'),
        frees: List<Char> = listOf('.'),
    ) {
        this.neighbors.filter { !obstacles.contains(it.value) }.forEach { neighbor ->
            neighbor.parsePosition(target, score + 1L, obstacles, frees)
        }
    }

    private fun parsePosition(
        target: Position2D,
        score: Long,
        obstacles: List<Char>,
        frees: List<Char>,
    ) {
        if (target.score != null && target.score!! < score) return

        val nextPosition = this
            .takeIf { (frees.contains(it.value) || it == target) }
            ?.takeIf { it.score == null || (it.score!! > score) }

        if (target == nextPosition) {
            if (target.score == null || target.score!! > score) {
                target.score = score
            }
        } else {
            nextPosition?.also { if (it.score == null || it.score!! > score) it.score = score }
                ?.calculateScoreOnMove(target, score, obstacles, frees)
        }
    }
}

data class Grid2D(val positions: List<Position2D>, val maxX: Int, val maxY: Int) {
    companion object {
        fun build(inputs: List<String>): Grid2D {
            val positions = inputs.parsePositionGrid()
            val maxX = (inputs.first().length - 1)
            val maxY = (inputs.size - 1)
            return Grid2D(positions, maxX, maxY)
        }
    }

    fun print(sequence: Sequence<Position2D>) {
        (0..this.maxY).forEach { y ->
            (0..this.maxX).forEach { x ->
                print(
                    sequence.find { it.x == x && it.y == y }?.value
                        ?: this.positions.first { it.x == x && it.y == y }.value
                )
            }
            println()
        }
        println()
    }
}

data class Grid2DWith4Neighbor(val positions: List<Position2D>, val maxX: Int, val maxY: Int) {
    companion object {
        fun build(positions: List<Position2D>, maxX: Int?, maxY: Int?): Grid2DWith4Neighbor {
            val realMaxY = maxY ?: positions.maxOf { it.y }
            val realMaxX = maxX ?: positions.maxOf { it.x }
            val allPositions = (0..realMaxY).flatMap { y ->
                (0..realMaxX).map { x ->
                    positions.firstOrNull { it.x == x && it.y == y } ?: Position2D(x, y, '.')
                }
            }
            return buildGridWithNeighbors(allPositions, realMaxX, realMaxY)
        }

        fun build(inputs: List<String>, filterOnValue: Boolean = false): Grid2DWith4Neighbor {
            val positions = inputs.parsePositionGrid()
            val maxX = (inputs.first().length - 1)
            val maxY = (inputs.size - 1)

            return buildGridWithNeighbors(positions, maxX, maxY, filterOnValue)
        }

        private fun buildGridWithNeighbors(positions: List<Position2D>, maxX: Int, maxY: Int, filterOnValue: Boolean = false) : Grid2DWith4Neighbor{
            positions.forEach { position ->
                positions.firstOrNull { it.x == position.x + 1 && it.y == position.y }
                    .takeIf { !filterOnValue || it?.value == position.value }?.let {
                        position.neighbors.add(it)
                    }

                positions.firstOrNull { it.x == position.x - 1 && it.y == position.y }
                    .takeIf { !filterOnValue || it?.value == position.value }?.let {
                        position.neighbors.add(it)
                    }

                positions.firstOrNull { it.x == position.x && it.y == position.y - 1 }
                    .takeIf { !filterOnValue || it?.value == position.value }?.let {
                        position.neighbors.add(it)
                    }

                positions.firstOrNull { it.x == position.x && it.y == position.y + 1 }
                    .takeIf { !filterOnValue || it?.value == position.value }?.let {
                        position.neighbors.add(it)
                    }
            }

            return Grid2DWith4Neighbor(positions, maxX, maxY)
        }
    }

    fun print() {
        (0..this.maxY).forEach { y ->
            (0..this.maxX).forEach { x ->
                print(
                    this.positions.first { it.x == x && it.y == y }.value
                )
            }
            println()
        }
        println()
    }

    fun print(sequence: Sequence<Position2D>) {
        (0..this.maxY).forEach { y ->
            (0..this.maxX).forEach { x ->
                print(
                    sequence.find { it.x == x && it.y == y }?.value
                        ?: this.positions.first { it.x == x && it.y == y }.value
                )
            }
            println()
        }
        println()
    }
}
