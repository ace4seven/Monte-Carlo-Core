import common.Constants
import controller.AppController
import javafx.beans.property.SimpleBooleanProperty
import javafx.geometry.Insets
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.Priority
import javafx.scene.text.FontWeight
import model.RoadType
import tornadofx.*
import java.lang.Error
import javax.xml.soap.Text

class AppView : View("Monte Carlo simulácia") {

    private val controller: AppController by inject()

    var xAxis : NumberAxis = NumberAxis()
    var yAxis : NumberAxis = NumberAxis()
    var startSimulationButton: Button by singleAssign()
    var pauseSimulationButton: Button by singleAssign()

    var lineChart: LineChart<Number, Number> by singleAssign()
    var chartSeries by singleAssign<XYChart.Series<Number, Number>>()

    var replicationTextField: TextField by singleAssign()
    var skipFactor: TextField by singleAssign()

    val traces = listOf<RoadType>(RoadType.AFHDE, RoadType.AFGE, RoadType.AFHCDE, RoadType.ABCDE).observable()

    override val root = tabpane {
        prefWidth = 1280.0
        prefHeight = 820.0
        tab("Simulácia") {
            vbox {
                form {
                    hbox(20) {
                        hboxConstraints {
                            marginLeft = 100.0
                        }
                        fieldset("Nastavenia simulácie") {
                            hbox(20) {
                                vbox {
                                    field("Počet replikácii") {
                                        textfield {
                                            replicationTextField = this
                                        }
                                    }
                                    field("Skip faktor") { textfield {
                                        skipFactor = this
                                    } }
                                }
                            }
                        }
                        fieldset("Trasa pre graf") {
                            field("Výber trasy") {
                                combobox(controller.selectedCitySelection, traces.map { it.title() }) {
                                    setOnAction {
                                        controller.updateTrace()
                                    }
                                }
                            }
                        }
                    }
                }

                vbox(20) {
                    hbox(20) {
                        vboxConstraints {
                            marginLeft = 20.0;
                        }
                        label("Aktuálny výsledok: ") {}
                        label() {
                            hboxConstraints {
                                marginTop = -8.0
                            }
                            bind(controller.currentResultProperty)
                            style {
                                fontSize = 25.px
                            }
                        }
                    }
                }

                lineChart = linechart("Graf", xAxis, yAxis) {
                    createSymbols = false
                    isLegendVisible = false
                    prefHeight = 600.0
                    maxHeight = 600.0
                    minWidth = 1200.0
                    animated = false

                    chartSeries = series(controller.chartTitleProperty.get(), controller.chartData)
                    with(yAxis as NumberAxis) {
                        label = "Priemerný čas prechodu [min]"
                        isForceZeroInRange = false
                        isAutoRanging = true
                    }
                    with(xAxis as NumberAxis) {
                        xAxis.label = "Číslo replikácie"
                        isForceZeroInRange = false
                        isAutoRanging = true
                    }

                }
                gridpane {
                    row {
                        button("Spusti simuláciu") {
                            startSimulationButton = this
                            gridpaneConstraints {
                                marginLeft = 10.0
                            }
                            action {
                                prepareToStart()
                                toggleButtons()
                            }
                        }

                        button("Pauza") {
                            pauseSimulationButton = this
                            gridpaneConstraints {
                                marginLeft = 10.0
                            }
                            action {
                                controller.pauseSimulation()
                                toggleButtons()
                            }
                        }

                        button("Reštart") {
                            gridpaneConstraints {
                                marginLeft = 10.0
                            }
                            action {
                                startSimulationButton.isDisable = false
                                pauseSimulationButton.isDisable = true
                                controller.restartSimulation()
                                lineChart.data.clear()
                                chartSeries.data.clear()
                                lineChart.series("Lorem", controller.chartData)
                            }
                        }
                    }
                }
            }
        }
        tab("Výsledky") {
           vbox(20) {
               label("Trasa A -> B -> C -> D -> E") {
                   style {
                       fontWeight = FontWeight.BOLD
                       fontSize = 20.px
                   }
                   vboxConstraints {
                       marginLeft = 20.0
                       marginTop = 10.0
                   }
               }
               label() {
                   bind(controller.traceABCDEProperty)

                   vboxConstraints {
                       marginLeft = 20.0
                       marginTop = 5.0
                   }
               }

               label("Trasa A -> F -> H -> D -> E: ") {
                   style {
                       fontWeight = FontWeight.BOLD
                       fontSize = 20.px
                   }
                   vboxConstraints {
                       marginLeft = 20.0
                       marginTop = 10.0
                   }
               }
               label() {
                   bind(controller.traceAFHDEProperty)

                   vboxConstraints {
                       marginLeft = 20.0
                       marginTop = 5.0
                   }
               }

               label("Trasa A -> F -> G -> E") {
                   style {
                       fontWeight = FontWeight.BOLD
                       fontSize = 20.px
                   }
                   vboxConstraints {
                       marginLeft = 20.0
                       marginTop = 10.0
                   }
               }
               label() {
                   bind(controller.traceAFGEProperty)

                   vboxConstraints {
                       marginLeft = 20.0
                       marginTop = 5.0
                   }
               }

               label("Trasa A -> F -> H -> C -> D -> E: ") {
                   style {
                       fontWeight = FontWeight.BOLD
                       fontSize = 20.px
                   }
                   vboxConstraints {
                       marginLeft = 20.0
                       marginTop = 10.0
                   }
               }
               label() {
                   bind(controller.traceAFHCDEProperty)

                   vboxConstraints {
                       marginLeft = 20.0
                       marginTop = 5.0
                   }
               }

           }
        }
    }

    fun toggleButtons() {
        startSimulationButton.isDisable = controller.simulationInProgressProperty.get()
        pauseSimulationButton.isDisable = !controller.simulationInProgressProperty.get()
    }

    fun prepareToStart() {
        controller.replicationCount = replicationTextField.getText().toLong()
        try {
            controller.skipFactor = skipFactor.getText().toDouble()
        } catch (error: Error) {
            print(error)
        }

        controller.startSimilation()
    }

}