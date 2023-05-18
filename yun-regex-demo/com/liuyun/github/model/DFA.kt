package com.liuyun.github.model

class DFA(states: List<Int>, transitions: List<Transition>, finalStates: Set<Int>) {
    /**
     * Initial state
     */
    var startState: Int

    /**
     * State set
     */
    var states: List<Int>

    /**
     * Collection of state transition functions
     */
    var transitions: List<Transition>

    /**
     * Final state set
     */
    var finalStates: Set<Int>

    /**
     * State transition function map
     */
    var transMap: MutableMap<Int?, MutableList<Transition?>?>? = null

    init {
        startState = states[0]
        this.states = states
        this.transitions = transitions
        this.finalStates = finalStates
    }

    fun recognize(word: String): Boolean {
        // Group by from field
        val transMap = transitions.groupBy { it.from }
        // Set current state as initial state
        var currentState = startState
        for (itemChar in word) {
            val transitions = transMap[currentState]
            transitions?.forEach { transition ->
                if (transition.symbol == itemChar) {
                    currentState = transition.to
                    return@forEach
                }
            }
        }
        return finalStates.contains(currentState)
    }

    fun display() {
        transitions.forEach { t -> println("(${t.from}, ${t.symbol}, ${t.to})") }
    }

}