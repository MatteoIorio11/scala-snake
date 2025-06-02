package core.entity

enum Direction:
  case UP, DOWN, LEFT, RIGHT

  def isOpposite(other: Direction): Boolean = (this, other) match
    case (UP, DOWN) | (DOWN, UP) | (LEFT, RIGHT) | (RIGHT, LEFT) => true
    case _ => false
