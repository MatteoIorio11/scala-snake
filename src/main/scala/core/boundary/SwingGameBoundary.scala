package core.boundary
import core.boundary.GameBoundary
import core.control.{GameConfig, GameController, GameState}
import core.entity.Direction

import javax.swing.*
import java.awt.*
import java.awt.event.*

class SwingGameBoundary extends GameBoundary {
  private var controller: GameController = _
  private var gameState: GameState = _
  private var frame: JFrame = _
  private var gamePanel: GamePanel = _
  private var scoreLabel: JLabel = _

  override def initialize(gameState: GameState): Unit = {
    this.gameState = gameState

    if (frame == null) {
      createFrame()
    } else {
      // Reset existing components
      gamePanel.setGameState(gameState)
      scoreLabel.setText("Score: 0")
      frame.repaint()
    }
  }

  private def createFrame(): Unit = {
    frame = new JFrame("Snake Game")
    frame.setDefaultCloseOperation(1)

    val config: GameConfig = gameState.getConfig;
    gamePanel = new GamePanel(gameState)

    val scorePanel = new JPanel()
    scoreLabel = new JLabel("Score: 0")
    scorePanel.add(scoreLabel)

    frame.setLayout(new BorderLayout())
    frame.add(gamePanel, BorderLayout.CENTER)
    frame.add(scorePanel, BorderLayout.SOUTH)

    frame.addKeyListener(new SnakeKeyListener)
    frame.setFocusable(true)

    val width = config.boardWidth * config.cellSize
    val height = config.boardHeight * config.cellSize
    gamePanel.setPreferredSize(new Dimension(width, height))
    frame.pack()
    frame.setResizable(false)
    frame.setLocationRelativeTo(null) // Center on screen
    frame.setVisible(true)
  }

  override def update(gameState: GameState): Unit = {
    this.gameState = gameState
    scoreLabel.setText("Score: " + gameState.getScore)
    gamePanel.repaint()
  }

  override def showGameOver(gameState: GameState): Unit = {
    JOptionPane.showMessageDialog(
      frame,
      "Game Over! Your score: " + gameState.getScore,
      "Game Over",
      JOptionPane.INFORMATION_MESSAGE
    )
  }

  override def showVictory(gameState: GameState): Unit = {
    JOptionPane.showMessageDialog(
      frame,
      "Congratulations! You won! Score: " + gameState.getScore,
      "Victory",
      JOptionPane.INFORMATION_MESSAGE
    )
  }

  override def setController(controller: GameController): Unit = {
    this.controller = controller
  }

  private class GamePanel(private var gameState: GameState) extends JPanel {
    setBackground(Color.BLACK)

    def setGameState(gameState: GameState): Unit = {
      this.gameState = gameState
    }

    override protected def paintComponent(g: Graphics): Unit = {
      super.paintComponent(g)
      val config = gameState.getConfig
      val cellSize = config.cellSize

      // Draw snake body
      g.setColor(Color.GREEN)
      for (p <- gameState.getSnake.getBody) {
        g.fillRect(p.x* cellSize, p.y* cellSize, cellSize, cellSize)
      }

      // Draw head with a distinct color
      val head = gameState.getSnake.getHead
      g.setColor(Color.YELLOW)
      g.fillRect(head.x* cellSize, head.y* cellSize, cellSize, cellSize)

      // Draw food
      g.setColor(Color.RED)
      val foodPos = gameState.getFood.position
      g.fillOval(foodPos.x* cellSize, foodPos.y* cellSize, cellSize, cellSize)

      // Draw grid (optional)
      g.setColor(Color.DARK_GRAY)
      for {
        x <- 0 until config.boardWidth
        y <- 0 until config.boardHeight
      } {
        g.drawRect(x * cellSize, y * cellSize, cellSize, cellSize)
      }
    }
  }

  private class SnakeKeyListener extends KeyAdapter {
    override def keyPressed(e: KeyEvent): Unit = {
      e.getKeyCode match {
        case KeyEvent.VK_UP    => controller.handleDirectionChange(Direction.UP)
        case KeyEvent.VK_DOWN  => controller.handleDirectionChange(Direction.DOWN)
        case KeyEvent.VK_LEFT  => controller.handleDirectionChange(Direction.LEFT)
        case KeyEvent.VK_RIGHT => controller.handleDirectionChange(Direction.RIGHT)
        case KeyEvent.VK_R     => controller.restart()
        case _ =>
      }
    }
  }
}
