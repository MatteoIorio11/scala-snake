package core.entity

import scala.collection.mutable.ArrayDeque

/**
 * Represents the Snake entity with its body and growth mechanism.
 *
 * @param initialPosition the starting position of the snake.
 */
class Snake(initialPosition: Position) {
  private val body: ArrayDeque[Position] = ArrayDeque(initialPosition)
  private var shouldGrow: Boolean = false

  /**
   * Returns the head of the snake.
   */
  def getHead: Position = body.head

  /**
   * Returns a list of positions representing the snake's body.
   */
  def getBody: List[Position] = body.toList

  /**
   * Returns the current length of the snake.
   */
  def getLength: Int = body.length

  /**
   * Triggers the snake to grow on its next move.
   */
  def grow(): Unit = {
    shouldGrow = true
  }

  /**
   * Moves the snake in the specified direction.
   * A new head is added in that direction.
   * If the snake is not marked to grow, the last tail element is removed.
   *
   * @param direction the direction in which the snake moves.
   */
  def move(direction: Direction): Unit = {
    val newHead = getHead.translate(direction)
    body.prepend(newHead)
    if (!shouldGrow) {
      body.removeLast()
    } else {
      shouldGrow = false
    }
  }

  /**
   * Checks if the snake occupies the given position.
   *
   * @param position the position to check.
   * @return true if the snake's body contains the position.
   */
  def occupies(position: Position): Boolean = body.contains(position)

  /**
   * Determines whether the snake's head collides with its body.
   *
   * @return true if the head occupies the same position as any other segment.
   */
  def collidesWithSelf: Boolean = {
    val head = getHead
    body.tail.exists(_ == head)
  }
}
