package controller

import com.apple.concurrent.Dispatch
import core.MCSimulationCore
import core.MCSimulationCoreObserver
import javafx.application.Platform
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.value.ObservableBooleanValue
import javafx.collections.FXCollections
import javafx.scene.chart.XYChart
import javafx.scene.control.Label
import model.RoadType
import sun.rmi.server.Dispatcher
import tornadofx.*
import kotlin.concurrent.thread

class AppController:  Controller(), MCSimulationCoreObserver {

    private var mc = MCSimulationCore(1, 10.0)

    var chartData = FXCollections.observableArrayList<XYChart.Data<Number, Number>>()

    val chartTitleProperty = SimpleStringProperty()
    var chartTitle: String by chartTitleProperty

    val simulationInProgressProperty = SimpleBooleanProperty()
    var simulationInProgress: Boolean by simulationInProgressProperty

    val currentResultProperty = SimpleStringProperty("0")
    var currentResult: String by currentResultProperty

    var replicationCount: Long? = null
    var skipFactor: Double? = null

    val traceABCDEProperty = SimpleStringProperty("0")
    val traceAFHCDEProperty = SimpleStringProperty("0")
    val traceAFHDEProperty = SimpleStringProperty("0")
    val traceAFGEProperty = SimpleStringProperty("0")

    var traceABCDE: String by traceABCDEProperty
    var traceAFHCDE: String by traceAFHCDEProperty
    var traceAFHDE: String by traceAFHDEProperty
    var traceAFGE: String by traceAFGEProperty

    val selectedCitySelection = SimpleStringProperty("")
    var selectedCity: String by selectedCitySelection



    init {
        mc.registerOberver(this)
        chartTitle = mc.roadForGraph.title()
    }

    fun startSimilation() {
        if (replicationCount == null) return
        mc.replication = replicationCount!!
        mc.delimeter = skipFactor!!
        simulationInProgress = true
        mc.start()
    }

    fun pauseSimulation() {
        simulationInProgress = false
        mc.pause()
    }

    fun restartSimulation() {
        mc.clear()

        traceABCDE = "0"
        traceAFHCDE = "0"
        traceAFGE = "0"
        traceAFHDE = "0"

        currentResult = "0"
    }

    fun updateTrace() {
        chartTitle = selectedCitySelection.get()

        when(selectedCitySelection.get()) {
            RoadType.ABCDE.title() -> mc.roadForGraph = RoadType.ABCDE
            RoadType.AFHCDE.title() -> mc.roadForGraph = RoadType.AFHCDE
            RoadType.AFGE.title() -> mc.roadForGraph = RoadType.AFGE
            RoadType.AFHDE.title() -> mc.roadForGraph = RoadType.AFHDE
        }
    }

    override fun refresh(core: MCSimulationCore) {
        Platform.runLater(Runnable {
            when(core.roadForGraph) {
                RoadType.ABCDE -> {
                    chartData.add(XYChart.Data(core.currentReplication, core.roadABCDE.averageTravel()))
                    currentResult = "${core.roadABCDE.averageTravel()}"
                }
                RoadType.AFHCDE -> {
                    chartData.add(XYChart.Data(core.currentReplication, core.roadAFHCDE.averageTravel()))
                    currentResult = "${core.roadAFHCDE.averageTravel()}"
                }
                RoadType.AFGE -> {
                    chartData.add(XYChart.Data(core.currentReplication, core.roadAFGE.averageTravel()))
                    currentResult = "${core.roadAFGE.averageTravel()}"
                }
                RoadType.AFHDE -> {
                    chartData.add(XYChart.Data(core.currentReplication, core.roadAFHDE.averageTravel()))
                    currentResult = "${core.roadAFHDE.averageTravel()}"
                }
            }

            traceABCDE = "Priemerny čas - ${core.roadABCDE.averageTravel()} | Pravdepodobnosť: ${core.roadABCDE.probability()}"
            traceAFHCDE = "Priemerny čas - ${core.roadAFHCDE.averageTravel()} | Pravdepodobnosť: ${core.roadAFHCDE.probability()}"
            traceAFGE = "Priemerny čas - ${core.roadAFGE.averageTravel()} | Pravdepodobnosť: ${core.roadAFGE.probability()}"
            traceAFHDE = "Priemerny čas - ${core.roadAFHDE.averageTravel()} | Pravdepodobnosť: ${core.roadAFHDE.probability()}"
        })
    }

}