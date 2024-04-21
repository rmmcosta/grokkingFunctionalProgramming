object main extends App {
  println("Testing add or resign:")
  testAddOrResign(addOrResign)
  println("Testing add or resign2:")
  testAddOrResign(addOrResign2)
  println("Testing add or resign3:")
  testAddOrResign(addOrResign3)
}

def addOrResign(
    parsedShows: Option[List[TvShow]],
    newShow: Option[TvShow]
): Option[List[TvShow]] = {
  newShow match {
    case Some(_) => Some(parsedShows.getOrElse(List()).appended(newShow.get))
    case None    => parsedShows
  }
}

def addOrResign2(
    parsedShows: Option[List[TvShow]],
    newShow: Option[TvShow]
): Option[List[TvShow]] = {
  (parsedShows, newShow) match {
    case (Some(_), Some(_)) => Some(parsedShows.get.appended(newShow.get))
    case _                  => None
  }
}

def addOrResign3(
    parsedShows: Option[List[TvShow]],
    newShow: Option[TvShow]
): Option[List[TvShow]] = {
  for {
    parsedShows <- parsedShows
    newShow <- newShow
  } yield parsedShows.appended(newShow)
}

def testAddOrResign(
    fn: (Option[List[TvShow]], Option[TvShow]) => Option[List[TvShow]]
) = {
  val friends = TvShow("Friends", 2000, 2009)
  val sopranos = TvShow("Sopranos", 1990, 1995)
  val empty1st = fn(Some(List.empty), Some(friends))
  print("Should be some list: ")
  println(empty1st)
  val emptyNone = fn(Some(List.empty), None)
  print("Should be none: ")
  println(emptyNone)
  val noneNone = fn(None, None)
  print("Should be none: ")
  println(noneNone)
  val noneSome = fn(None, Some(friends))
  print("Should be none: ")
  println(noneSome)
  val someNone = fn(Some(List(friends)), None)
  print("Should be none: ")
  println(someNone)
}

case class TvShow(
    title: String,
    startYear: Int,
    endYear: Int
)
