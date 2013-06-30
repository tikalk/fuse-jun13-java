package logic

import infrastructure._

class PointOps(val point: Point) {
  def +(dir: Direction): Point = {
    dir match {
      case Direction.LEFT => new Point(point.getX - 1, point.getY)
      case Direction.RIGHT => new Point(point.getX + 1, point.getY)
      case Direction.UP => new Point(point.getX, point.getY - 1)
      case Direction.DOWN => new Point(point.getX, point.getY + 1)
      case _ => point
    }
  }
}

object PointOps{
  implicit def pointToPointOps(value: Point): PointOps = new PointOps(value)
}
