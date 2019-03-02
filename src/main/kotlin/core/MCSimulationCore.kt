package core

interface MCSimulationCoreObserver {
    fun refresh(core: MCSimulationCore)
}

class MCSimulationCore(replication: Long, delimeter: Double) {

    private var observers = arrayListOf<MCSimulationCoreObserver>()

    fun registerOberver(observer: MCSimulationCoreObserver) {
        observers.add(observer)
    }
}