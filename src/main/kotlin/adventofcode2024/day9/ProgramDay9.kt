package adventofcode2024.day9

import common.Program

class ProgramDay9(brutInputs: List<String>, private val debug: Boolean = false) : Program {
    private val blocks = brutInputs.first().mapIndexed { index, digit ->
        if (index % 2 == 0) {
            Block(
                index = index.toLong(),
                list = (0 until digit.toString().toLong()).map { index / 2L }.toMutableList()
            )
        } else {
            Block(index = index.toLong(), freeSpace = digit.toString().toLong())
        }
    }

    override fun part1(): String {
        val files = blocks.filter { it.freeSpace == null }
        val freeSpaces = blocks.filter { it.freeSpace != null }

        files.reversed().forEach { block ->
            val list = block.list.map { it }
            list.forEach { fileValue ->
                val freespace = freeSpaces.firstOrNull { it.freeSpace != 0L && it.index < block.index }
                if (freespace != null) {
                    freespace.list.add(fileValue)
                    freespace.freeSpace = (freespace.freeSpace ?: 1) - 1
                    block.list.removeFirst()
                }
            }

        }

        return blocks.filter { it.list.isNotEmpty() }.flatMap {
            it.list
        }.mapIndexed { index, i -> index * i }.sum().toString()
    }

    override fun part2(): String {
        val files = blocks.filter { it.freeSpace == null }
        val freeSpaces = blocks.filter { it.freeSpace != null }

        files.reversed().forEach { block ->
            val list = block.list.map { it }
            val freespace = freeSpaces.firstOrNull { (it.freeSpace ?: 0) >= list.size && it.index < block.index }
            if (freespace != null) {
                freespace.list.addAll(list)
                freespace.freeSpace = (freespace.freeSpace ?: 0) - list.size
                block.freeSpace = block.list.size.toLong()
                block.list.clear()
            }
        }

        return blocks.flatMap {
            it.list.plus((0 until (it.freeSpace ?: 0)).map { 0 })
        }.mapIndexed { index, i -> index * i }.sum().toString()
    }

    data class Block(val index: Long, val list: MutableList<Long> = mutableListOf(), var freeSpace: Long? = null)
}
