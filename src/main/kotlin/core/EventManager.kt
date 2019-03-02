package core

import core.generators.CEvenGenerator
import core.generators.DEmpiricGenerator
import core.generators.DEvenGenerator
import java.util.Random

class EventManager(val seedGenerator: Random) {

    val eventAB: CEvenGenerator
    val eventBC: CEvenGenerator
    val eventCD: CEvenGenerator
    val eventDE: CEvenGenerator
    val eventAF: CEvenGenerator
    val eventFG: DEmpiricGenerator
    val eventFH: CEvenGenerator
    val eventHC: CEvenGenerator
    val eventHD: CEvenGenerator
    val eventGE: DEvenGenerator

    val eventHDComplication: CEvenGenerator

    init {
        eventAB = CEvenGenerator(170.toDouble(), 217.toDouble(), seedGenerator.nextLong())
        eventBC = CEvenGenerator(120.toDouble(), 230.toDouble(), seedGenerator.nextLong())
        eventCD = CEvenGenerator(50.toDouble(), 70.toDouble(), seedGenerator.nextLong())
        eventDE = CEvenGenerator(19.toDouble(), 36.toDouble(), seedGenerator.nextLong())
        eventAF = CEvenGenerator(150.toDouble(), 240.toDouble(), seedGenerator.nextLong())
        eventFG = DEmpiricGenerator(Pair(170, 196), Pair(196, 281), 0.2, Triple(seedGenerator.nextLong(), seedGenerator.nextLong(), seedGenerator.nextLong()))
        eventFH = CEvenGenerator(30.toDouble(), 62.toDouble(), seedGenerator.nextLong())
        eventHC = CEvenGenerator(150.toDouble(), 220.toDouble(), seedGenerator.nextLong())
        eventHD = CEvenGenerator(170.toDouble(), 200.toDouble(), seedGenerator.nextLong())
        eventGE = DEvenGenerator(20, 50, seedGenerator.nextLong())

        eventHDComplication = CEvenGenerator(0.0, 1.0, seedGenerator.nextLong())
    }

    fun traceABCDE(): Double {
        return eventAB.nextDouble() + eventBC.nextDouble() + eventCD.nextDouble() + eventDE.nextDouble()
    }

    fun traceAFHCDE(): Double {
        return eventAF.nextDouble() + eventFH.nextDouble() + eventHC.nextDouble() + eventCD.nextDouble() + eventDE.nextDouble()
    }

    fun traceAFHDE(): Double {
        if (eventHDComplication.nextDouble() < 0.05) {
            return traceAFHCDE()
        } else {
            return eventAF.nextDouble() + eventFH.nextDouble() + eventHD.nextDouble() + eventDE.nextDouble()
        }
    }

    fun traceAFGE(): Double {
        return eventAF.nextDouble() + eventFG.nextInt().toDouble() + eventGE.nextInt().toDouble()
    }


}