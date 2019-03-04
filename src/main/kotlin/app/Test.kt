package app

import core.MCSimulationCore

fun main(args: Array<String>) {
    val monteCarlo = MCSimulationCore(10000000, 10.0)

    monteCarlo.start()
    monteCarlo.testResults()
}