package model

enum class RoadType {
    ABCDE, AFHCDE, AFHDE, AFGE
}

data class DataEntry(val type: RoadType, val replications: Long) {

    private var goodTry: Int = 0
    private var minutesSum: Double = 0.0
    private var currentReplication: Int = 0

    fun updateData(currentReplication: Int, minutesSum: Double) {
        this.currentReplication = currentReplication
        this.minutesSum += minutesSum
    }

    fun averageTravel(): Double {
        return minutesSum / currentReplication
    }

    fun incGoodTry() {
        goodTry += 1
    }

    fun probability(): Double {
        return goodTry / replications.toDouble()
    }

    fun title(): String {
        when(type) {
            RoadType.ABCDE -> return "Trasa A->B->C->DE"
            RoadType.AFHCDE -> return "Trasa A->F->H->C->DE"
            RoadType.AFGE -> return "Trasa A->F->G->E"
            RoadType.AFHDE -> return "Trasa A->F->H->D->E"
        }
    }

}