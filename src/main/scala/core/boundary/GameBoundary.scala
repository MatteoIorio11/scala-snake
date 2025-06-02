package core.boundary

import core.control.{GameController, GameState}

trait GameBoundary:
  def initialize(gameControl: GameState): Unit
  def update(gameControl: GameState): Unit
  def showGameOver(gameControl: GameState): Unit
  def showVictory(gameControl: GameState): Unit
  def setController(gameController: GameController): Unit

