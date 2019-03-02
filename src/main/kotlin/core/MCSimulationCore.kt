package core

import common.Constants
import model.DataEntry
import model.RoadType
import java.util.*
import kotlin.reflect.jvm.internal.impl.load.java.Constant

interface MCSimulationCoreObserver {
    fun refresh(core: MCSimulationCore)
}

interface MCSimulationCoreDelegate {
    fun start()
    fun pause()
    fun clear()
}

class MCSimulationCore(val replication: Long, val delimeter: Double): MCSimulationCoreDelegate {

    private var observers = arrayListOf<MCSimulationCoreObserver>()
    private var seedGenerator: Random

    private var currentReplication: Int = 0
    private var simulationInProgress: Boolean = false

    private var eventManager: EventManager

    private var roadAFHDE: DataEntry = DataEntry(RoadType.AFHDE, replication)
    private var roadAFGE: DataEntry = DataEntry(RoadType.AFGE, replication)
    private var roadAFHCDE: DataEntry = DataEntry(RoadType.AFHCDE, replication)
    private var roadABCDE: DataEntry = DataEntry(RoadType.ABCDE, replication)

    init {
        this.seedGenerator  = Random()
        this.eventManager   = EventManager(seedGenerator)
    }

    override fun start() {
        simulationInProgress = true
        monteCarlo()
    }

    override fun pause() {
        simulationInProgress = false
        monteCarlo()
    }

    override fun clear() {

    }


    fun registerOberver(observer: MCSimulationCoreObserver) {
        this.observers.add(observer)
    }

    fun testResults() {
        println("roadABCDE -> ${roadABCDE.averageTravel()} p: ${roadABCDE.probability()}")
        println("roadAFGE -> ${roadAFGE.averageTravel()} p: ${roadAFGE.probability()}")
        println("roadAFHCDE -> ${roadAFHCDE.averageTravel()} p: ${roadAFHCDE.probability()}")
        println("roadAFHDE -> ${roadAFHDE.averageTravel()} p: ${roadAFHDE.probability()}")
    }

    private fun monteCarlo() {
        while(currentReplication < replication && simulationInProgress ) {
            currentReplication += 1

            var result = eventManager.traceABCDE()
            roadABCDE.updateData(currentReplication, result)
            if (result < Constants.timeBorder) { roadABCDE.incGoodTry() }

            result = eventManager.traceAFGE()
            roadAFGE.updateData(currentReplication, result)
            if (result < Constants.timeBorder) { roadAFGE.incGoodTry() }

            result = eventManager.traceAFHCDE()
            roadAFHCDE.updateData(currentReplication, result)
            if (result < Constants.timeBorder) { roadAFHCDE.incGoodTry() }

            result = eventManager.traceAFHDE()
            roadAFHDE.updateData(currentReplication, result)
            if (result < Constants.timeBorder) { roadAFHDE.incGoodTry() }
        }

        testResults()
    }
}