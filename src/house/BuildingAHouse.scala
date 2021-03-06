package house

sealed trait Lot {
  def name: Char
  def usable: Boolean
}

object Lot {
  def forName(c: Char): Lot = c match {
    case 'G' => Grass
    case 'R' => Rock
    case 'W' => Water
    case 'S' => Shrubs
    case 'T' => Trees
  }
}
object Grass extends Lot {
  val name = 'G'
  val usable = true
}
object Rock extends Lot {
  val name = 'R'
  val usable = false
}
object Water extends Lot {
  val name = 'W'
  val usable = false
}
object Shrubs extends Lot {
  val name = 'S'
  val usable = true
}
object Trees extends Lot {
  val name = 'T'
  val usable = false
}

case class Land(lots: Array[Array[Lot]]) {
  override def toString = lots.foldLeft("")((acc, row) => acc + row.foldLeft("")((a, lot) => a + lot.name) + "\n")

  def length = lots(0).length
  def width = lots.length

  def usable(x: Int, y: Int, w: Int, h: Int) = {
    val bs = for {
      posx <- x until x+w
      posy <- y until y+h
    } yield lots(posx)(posy)
    bs.foldLeft(true)((acc, lot) => acc && lot.usable)
  }

  def apply(x: Int, y: Int) = lots(x)(y)
}

object Land {
  def apply(ss: List[String]) =
    new Land(
      ss.map(s => s.toCharArray.map(Lot.forName(_))).toArray
    )
}

object BuildingAHouse extends App {

  import scala.io._

  val lines = Source.fromFile("src/house/C-large-practice.in").getLines
  lines.next
  var c = 1

  while (lines.hasNext) {
    val h = lines.next.split(" ")(1).toInt

    val plan = Range(0, h).map(i => lines.next).toList
    val res = maxRect(plan)

    println("Case #" + c + ": " + res)
    c = c + 1
  }

  def maxRect(plan: List[String]): Int = {
    val land = Land(plan)

    val cs = for {
      x <- 0 until land.lots.length
      y <- 0 until land.lots(x).length
      w <- 1 to land.lots.length - x
      l <- 1 to land.lots(x).length - y

      if land.usable(x,y,w,l)
    } yield w*l

    cs.max
  }
}
