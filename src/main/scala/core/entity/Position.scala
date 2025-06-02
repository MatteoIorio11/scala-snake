package core.entity

case class Position(x: Int, y: Int) {
  def translate(direction: Direction): Position = direction match {
    case Direction.UP    => Position(x, y - 1)
    case Direction.DOWN  => Position(x, y + 1)
    case Direction.LEFT  => Position(x - 1, y)
    case Direction.RIGHT => Position(x + 1, y)
    case _ => throw new IllegalArgumentException("Invalid direction")
  }
}

