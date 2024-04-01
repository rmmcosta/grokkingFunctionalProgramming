object main extends App {
  val rawTvShows = List(
    "The Wire (2009-2018)",
    "Chernobyl (2019)",
    "Westworld (2016-2020)",
    "The Mandalorian (2019-)",
    "The Crown (2016-)",
    "The Queen's Gambit (-2020)",
    "Breaking Bad (-)",
    "Game of Thrones",
    "(2018-2020)",
    "The Sopranos (1999-2007)",
    "Wrong year (xpto-abcd)"
  )

  val onlyValidTvShows = List(
    "The Wire (2009-2018)",
    "Chernobyl (2019)",
    "Westworld (2016-2020)",
    "The Sopranos (1999-2007)"
  )

  println(s"Should print None: ${parseShows(rawTvShows)}")
  println(s"Should print some list: ${parseShows(onlyValidTvShows)}")
}

case class TvShow(title: String, startYear: Int, endYear: Int)

def parseShows(rawTvShows: List[String]): Option[List[TvShow]] =
  rawTvShows.map(parseShow).foldLeft(Some(List.empty[TvShow]))(addOrResign)

def addOrResign(
    list: Option[List[TvShow]],
    toAdd: Option[TvShow]
): Option[List[TvShow]] = for {
  existingList <- list
  newShow <- toAdd
} yield existingList.appended(newShow)

def parseShow(rawTvShow: String): Option[TvShow] = for {
  title <- extractTitle(rawTvShow)
  startYear <- extractStartYear(rawTvShow)
  endYear <- extractEndYear(rawTvShow)
} yield TvShow(title, startYear, endYear)

def extractTitle(rawTvShow: String): Option[String] = {
  val openParenthesis = rawTvShow.indexOf("(")
  if (openParenthesis < 1) {
    None
  } else {
    Some(rawTvShow.substring(0, openParenthesis).trim)
  }
}

def extractStartYear(rawTvShow: String): Option[Int] = {
  val openParenthesis = rawTvShow.indexOf("(")
  val dash = rawTvShow.indexOf("-")
  if (dash - openParenthesis != 5) {
    extractSingleYear(rawTvShow)
  } else {
    val year = rawTvShow.substring(openParenthesis + 1, dash)
    year.toIntOption
  }
}

def extractEndYear(rawTvShow: String): Option[Int] = {
  val dash = rawTvShow.indexOf("-")
  val closeParenthesis = rawTvShow.indexOf(")")
  if (closeParenthesis - dash != 5) {
    extractSingleYear(rawTvShow)
  } else {
    val year = rawTvShow.substring(dash + 1, closeParenthesis)
    year.toIntOption
  }
}

def extractSingleYear(rawTvShow: String): Option[Int] = {
  val openParenthesis = rawTvShow.indexOf("(")
  val closeParenthesis = rawTvShow.indexOf(")")
  if (closeParenthesis - openParenthesis != 5) {
    None
  } else {
    val year = rawTvShow.substring(openParenthesis + 1, closeParenthesis)
    year.toIntOption
  }
}
