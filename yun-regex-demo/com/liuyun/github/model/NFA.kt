package com.liuyun.github.model

import com.liuyun.github.utils.Constants
import java.util.*

class NFA {

    @JvmField
    var startState = 0
    @JvmField
    var states = mutableListOf<Int>()
    @JvmField
    var transitions = mutableListOf<Transition>()
    @JvmField
    var finalState = 0

    constructor() {
        states.add(startState)
    }

    constructor(size: Int) {
        states.add(startState)
        setStateSize(size)
    }

    constructor(c: Char) {
        states.add(startState)
        setStateSize(2)
        finalState = 1
        transitions.add(Transition(0, 1, c))
    }

    fun setStateSize(size: Int) {
        states.clear()
        states.addAll(0 until size)
    }

    fun getStartStates(): Set<Int> {
        return setOf(states[0])
    }

    fun getSymbolSet(): Set<Char> {
        return transitions.filter { it.symbol != Constants.EPSILON }.map { it.symbol }.toSet()
    }

    fun delta(stateSet: Set<Int>, symbol: Char): Set<Int> {
        return transitions.filter { stateSet.contains(it.from) && it.symbol == symbol }.map { it.to }.toSet()
    }

    fun epsilonClosures(stateSet: Set<Int>): Set<Int> {
        val resultSet = mutableSetOf<Int>()
        val queue: Queue<Int> = LinkedList()
        queue.addAll(stateSet)
        while (queue.isNotEmpty()) {
            val state = queue.poll()
            resultSet.add(state)
            transitions.filter { it.from == state && it.symbol == Constants.EPSILON && it.to !in resultSet }
                    .forEach { queue.add(it.to) }
        }
        return resultSet
    }

    fun display() {
        transitions.forEach { t -> println("(${t.from}, ${t.symbol}, ${t.to})") }
    }

}
