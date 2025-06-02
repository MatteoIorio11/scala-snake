package core.control

import core.entity.{Direction, Food, Position, Snake}

import java.util.Random

/**
 * Represents the state of the game.
 *
 * @param config the game configuration.
 */
class GameState(val config: GameConfig) {

  // The snake is initialized in the center of the board.
  val snake: Snake = new Snake(new Position(config.boardWidth / 2, config.boardHeight / 2))

  // Mutable state variables.
  private var food: Food = generateFood()
  private var score: Int = 0
  private var currentDirection: Direction = Direction.RIGHT
  // Using _gameOver as the private variable to avoid naming conflict with the getter.
  private var _gameOver: Boolean = false

  // Public getters
  def getSnake: Snake = snake
  def getFood: Food = food
  def getScore: Int = score
  def getCurrentDirection: Direction = currentDirection
  def isGameOver: Boolean = _gameOver
  def getConfig: GameConfig = config

  /**
   * Sets the new direction for the snake if it is not perpendicular to the current direction.
   *
   * @param newDirection the new direction.
   */
  def setDirection(newDirection: Direction): Unit = {
    if (!currentDirection.isOpposite(newDirection)) {
      currentDirection = newDirection
    }
  }

  /**
   * Increments the score by one.
   */
  def incrementScore(): Unit = {
    score += 1
  }

  /**
   * Marks the game as over.
   */
  def setGameOver(): Unit = {
    _gameOver = true
  }

  /**
   * Sets the food to a new Food instance.
   *
   * @param newFood the new food object.
   */
  def setFood(newFood: Food): Unit = {
    food = newFood
  }



  /**
   * Generates a food item at a random position on the board that is not occupied by the snake.
   *
   * @return a new Food instance.
   */
  private def generateFood(): Food = {
    val random = new Random
    var x, y = 0
    import scala.annotation.tailrec

    @tailrec
    def generatePosition(): Position = {
      val x = random.nextInt(config.boardWidth)
      val y = random.nextInt(config.boardHeight)
      val pos = new Position(x, y)
      if (snake.occupies(pos)) generatePosition() else pos
    }

    val position = generatePosition()
    new Food(position)
  }

  /**
   * Checks whether the game has been won.
   *
   * @return true if the snake occupies all board cells except one.
   */
  def isGameWon: Boolean = {
    snake.getLength == config.boardWidth * config.boardHeight - 1
  }
}

