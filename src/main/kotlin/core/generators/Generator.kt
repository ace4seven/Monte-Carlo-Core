package core

import java.util.*

abstract class Generator {

    protected val randomGenerator: Random

    constructor(seed: Long?) {
        if (seed != null) {
            this.randomGenerator = Random(seed)
        } else {
            this.randomGenerator = Random()
        }
    }

}

class DiscreteGenerator(minValue: Int, maxValue: Int, seed: Long? = null): Generator(seed) {

    val min = minValue
    val max: Int = maxValue

    fun generate(): Int {
        return randomGenerator.nextInt(max - min) + min
    }

}

class ContinuosGenerator(minValue: Double = 0.0, maxValue: Double = 1.0, seed: Long? = null): Generator(seed) {

    val min = minValue
    val max = maxValue

    fun generate(): Double {
        return min + (max - min) * randomGenerator.nextDouble()
    }

}