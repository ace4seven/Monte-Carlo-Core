package core.generators

interface DEmpiricGeneratorDelegate {
    fun nextInt(): Int
}

class DEmpiricGenerator(t1: Pair<Int, Int>, t2: Pair<Int, Int>, val p: Double, seed: Triple<Long, Long, Long>): DEmpiricGeneratorDelegate {

    private val pGenerator: ContinuosGenerator
    private val d1Generator: DiscreteGenerator
    private val d2Generator: DiscreteGenerator

    init {
        this.pGenerator = ContinuosGenerator(0.toDouble(), 1.toDouble(), seed.first)
        this.d1Generator = DiscreteGenerator(t1.first, t1.second, seed.second)
        this.d2Generator = DiscreteGenerator(t2.first, t2.second, seed.third)
    }

    override fun nextInt(): Int {
        if (pGenerator.generate() < p) {
            return d1Generator.generate()
        } else {
            return d2Generator.generate()
        }
    }

}