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
    "(2018-2020)"
  )

  println(rawTvShows)
  println(TvShow.parseShow(rawTvShows.apply(0)))
  println(TvShow.parseShow(rawTvShows.apply(1)))
  println(TvShow.parseShow(rawTvShows.apply(2)))
  println(TvShow.parseShow(rawTvShows.apply(3)))
  println(TvShow.parseShow(rawTvShows.apply(4)))
  println(TvShow.parseShow(rawTvShows.apply(5)))
  // print(TvShow.parseShows(rawTvShows))
}

case class TvShow(title: String, startYear: Int, endYear: Int)

object TvShow {
  def parseShows(rawTvShows: List[String]): Either[String, List[TvShow]] = {
    val parsedTvShows = rawTvShows.map(parseShow)
    if (parsedTvShows.exists(_.isLeft)) {
      Left(parsedTvShows.filter(_.isLeft).map(_.left.get).mkString(", "))
    } else {
      Right(parsedTvShows.map(_.right.get))
    }
  }
  def parseShow(rawTvShow: String): Either[String, TvShow] = {
    for {
      title <- extractTitle(rawTvShow)
      startYear <- extractStartYear(rawTvShow)
      endYear <- extractEndYear(rawTvShow)
    } yield TvShow(title, startYear, endYear)
  }
  def extractTitle(rawTvShow: String): Either[String, String] = {
    val spaceIndex = rawTvShow.indexOf(" (")
    if (spaceIndex != -1) {
      Right(rawTvShow.substring(0, spaceIndex))
    } else {
      Left("Failed to parse the title")
    }
  }
  def extractStartYear(rawTvShow: String): Either[String, Int] = {
    val startYearIndex = rawTvShow.indexOf("(")
    val endYearIndex = rawTvShow.indexOf("-")
    if (startYearIndex != -1 && endYearIndex != -1 && endYearIndex - startYearIndex == 5) {
      Right(rawTvShow.substring(startYearIndex + 1, endYearIndex).toInt)
    } else {
      Left("Failed to parse the start year")
    }
  }
  def extractEndYear(rawTvShow: String): Either[String, Int] = {
    val endYearIndex = rawTvShow.indexOf("-")
    val endYearIndex2 = rawTvShow.indexOf(")")
    if (endYearIndex != -1 && endYearIndex2 != -1 && endYearIndex2 - endYearIndex == 5) {
      Right(rawTvShow.substring(endYearIndex + 1, endYearIndex2).toInt)
    } else {
      Left("Failed to parse the end year")
    }
  }
  def extractSingleYear(rawTvShow: String): Either[String, Int] = {
    val startYearIndex = rawTvShow.indexOf("(")
    val endYearIndex = rawTvShow.indexOf(")")
    if (
      startYearIndex != -1 && endYearIndex != -1 && endYearIndex - startYearIndex == 5
    ) {
      Right(rawTvShow.substring(startYearIndex + 1, endYearIndex).toInt)
    } else {
      Left("Failed to parse the single year")
    }
  }
}
