package core.control

import core.boundary.GameBoundary
import core.entity.{Direction, Food, Position}

import javax.swing.Timer
import java.awt.event.ActionEvent
import scala.annotation.tailrec

class GameController(config: GameConfig, view: GameBoundary) {
  // Initialize game state and set this controller in the view.
  private val gameState: GameState = new GameState(config)
  view.setController(this)

  def start(): Unit = {
    initializeTimer()
    view.initialize(gameState)
  }

  def handleDirectionChange(direction: Direction): Unit =
    gameState.setDirection(direction)

  def restart(): Unit = {
    // Create a new game state with the same configuration.
    val newState = new GameState(gameState.getConfig)
    gameState.setGameOver()
    // Reinitialize the view with the new state.
    view.initialize(newState)
  }

  private def initializeTimer(): Unit = {
    val delay = gameState.getConfig.updateIntervalMs
    // SAM conversion in Scala lets us use a lambda for the ActionListener.
    new Timer(delay, (e: ActionEvent) => updateGame()).start()
  }

  private def updateGame(): Unit = {
    if (gameState.isGameOver) return

    // Move the snake in the current direction.
    gameState.getSnake.move(gameState.getCurrentDirection)
    val head: Position = gameState.getSnake.getHead
    val config0 = gameState.getConfig

    // Check wall collision.
    if (head.x < 0 || head.x >= config0.boardWidth || head.y < 0 || head.y >= config0.boardHeight) {
      gameState.setGameOver()
      view.showGameOver(gameState)
    }
    // Check self collision.
    else if (gameState.getSnake.collidesWithSelf) {
      gameState.setGameOver()
      view.showGameOver(gameState)
    }
    // Check food collision.
    else if (head == gameState.getFood.position) {
      gameState.incrementScore()
      gameState.getSnake.grow()
      gameState.setFood(generateFood())

      // Check win condition.
      if (gameState.isGameWon) {
        gameState.setGameOver()
        view.showVictory(gameState)
      } else {
        view.update(gameState)
      }
    }
    // Otherwise, simply update the view.
    else {
      view.update(gameState)
    }
  }

  private def generateFood(): Food = {
    val random = new java.util.Random
    val config0 = gameState.getConfig

    // Tail-recursive helper function to generate an unoccupied position.
    @tailrec
    def loop(): Position = {
      val x = random.nextInt(config0.boardWidth)
      val y = random.nextInt(config0.boardHeight)
      val pos = new Position(x, y)
      if (gameState.getSnake.occupies(pos)) loop() else pos
    }

    new Food(loop())
  }
}
