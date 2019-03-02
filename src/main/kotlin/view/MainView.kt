import tornadofx.*

class MainView : View("Semestrálna práca 1") {
    override val root = hbox {
        label(title) {
            addClass(Styles.heading)
        }
    }
}