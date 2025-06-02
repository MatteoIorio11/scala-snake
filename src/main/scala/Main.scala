import core.boundary.{GameBoundary, SwingGameBoundary}
import core.control.{GameConfig, GameController}

@main def hello(): Unit =
  javax.swing.SwingUtilities.invokeLater(() => {
    val gameConfig:GameConfig = GameConfig(20, 20, 20, 150)
    val view:GameBoundary = SwingGameBoundary()
    val controller:GameController = GameController(gameConfig, view)
    controller.start()
  })
def msg = "I was compiled by Scala 3. :)"
