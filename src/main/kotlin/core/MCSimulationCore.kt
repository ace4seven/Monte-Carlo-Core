package core

import com.sun.javafx.tools.ant.Platform
import common.Constants
import model.DataEntry
import model.RoadType
import java.util.*

interface MCSimulationCoreObserver {
    fun refresh(core: MCSimulationCore)
}

interface MCSimulationCoreDelegate {
    fun start()
    fun pause()
    fun clear()
}

class MCSimulationCore(var replication: Long, val delimeter: Double): MCSimulationCoreDelegate {

    private var observers = arrayListOf<MCSimulationCoreObserver>()
    private var seedGenerator: Random

    var currentReplication: Int = 0
    private var simulationInProgress: Boolean = false

    private var eventManager: EventManager

    var roadAFHDE: DataEntry = DataEntry(RoadType.AFHDE)
    var roadAFGE: DataEntry = DataEntry(RoadType.AFGE)
    var roadAFHCDE: DataEntry = DataEntry(RoadType.AFHCDE)
    var roadABCDE: DataEntry = DataEntry(RoadType.ABCDE)

    var jumpDrawOnChart: Int = 0
    var jumpIndex: Int = 0

    var roadForGraph: RoadType = RoadType.ABCDE

    init {
        this.seedGenerator  = Random()
        this.eventManager   = EventManager(seedGenerator)
    }

    override fun start() {
        jumpDrawOnChart = (replication / Constants.maxChartPoints).toInt()
        simulationInProgress = true
        monteCarlo()
    }

    override fun pause() {
        simulationInProgress = false
        monteCarlo()
    }

    override fun clear() {
        roadAFHDE = DataEntry(RoadType.AFHDE)
        roadAFGE = DataEntry(RoadType.AFGE)
        roadAFHCDE = DataEntry(RoadType.AFHCDE)
        roadABCDE = DataEntry(RoadType.ABCDE)

        jumpDrawOnChart = 0
        jumpIndex = 0

        currentReplication = 0
        simulationInProgress = false
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
        val self = this

        val thread = object: Thread(){
            override fun run(){
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

                    if ((currentReplication.toDouble() / replication.toDouble()) > Constants.balast) {
                        if(jumpIndex > jumpDrawOnChart) {
                            observers.forEach {
                                it.refresh(self)
                            }
                            jumpIndex = 0
                            Thread.sleep(1)
                        } else {
                            jumpIndex += 1
                        }
                    }
                }

                testResults()
            }
        }

        thread.start()
    }

}