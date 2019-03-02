package app

import core.generators.ContinuosGenerator
import core.generators.DiscreteGenerator
import core.generators.DEmpiricGenerator
import java.util.*

fun main(args: Array<String>) {
    val r = Random()
    val d = DEmpiricGenerator(Pair(170, 195), Pair(196, 280), 0.2, Triple(r.nextLong(), r.nextLong(), r.nextLong()))

    val testDiscrete = DiscreteGenerator(1, 100 + 1)
    val continuosGenerator = ContinuosGenerator()

    for (i in 0..100000) {
        println(continuosGenerator.generate())
    }
}