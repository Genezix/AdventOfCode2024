package common

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

data class Position2D(val value: Char, val x: Int, val y: Int)

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
