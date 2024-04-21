object Main extends App {
  println(s"should print the list: ${parseShows(getGoodSampleData)}")
  println(
    s"should print unable to parse the end year: ${parseShows(getBadSampleData)}"
  )
  println(
    s"should print unable to parse the start year: ${parseShows(getBadSampleData.slice(2, 3))}"
  )
  println(
    s"should print unable to parse the start year: ${parseShows(getBadSampleData.slice(3, 4))}"
  )
  println(
    s"should print unable to parse the start year: ${parseShows(getBadSampleData.slice(4, 5))}"
  )
  println(
    s"should print unable to parse the title: ${parseShows(getBadSampleData.slice(5, 6))}"
  )
  println(
    s"should print unable to parse the title: ${parseShows(getBadSampleData.slice(6, 7))}"
  )
  println(
    s"should print unable to parse the title: ${parseShows(getBadSampleData.slice(7, 8))}"
  )
  println(
    s"should print start year is not a valid year: ${parseShows(getBadSampleData.slice(8, 9))}"
  )
  println(
    s"should print end year is not a valid year: ${parseShows(getBadSampleData.slice(9, 10))}"
  )
}

def getGoodSampleData = List(
  "The Wire (2015-2018)",
  "Friends (1998-2007)",
  "Sopranos (1986-1994)",
  "Suits (2012-2019)",
  "Chernobyl (2008)"
)

def getBadSampleData = List(
  "The Wire (2015-2018)",
  "Friends (1998-)",
  "Sopranos (-1994)",
  "Suits (-)",
  "Batman",
  "()",
  "",
  "(2019-2029)",
  "How I met your mother (xpto-abcd)",
  "How I met your mother (2020-abcd)"
)

case class TvShow(title: String, startYear: Int, endYear: Int)

def parseShows(rawTvShows: List[String]): Either[String, List[TvShow]] = {
  rawTvShows
    .map(parseShow)
    .foldLeft(Right(List.empty[TvShow]): Either[String, List[TvShow]])(
      addOrResign
    )
}

def addOrResign(
    list: Either[String, List[TvShow]],
    tvShow: Either[String, TvShow]
): Either[String, List[TvShow]] = for {
  currentList <- list
  currentTvShow <- tvShow
} yield currentList :+ currentTvShow

def parseShow(rawTvShow: String): Either[String, TvShow] = for {
  title <- extractTitle(rawTvShow)
  startYear <- extractSingleYear(rawTvShow).orElse(
    extractStartYear(rawTvShow)
  )
  endYear <- extractSingleYear(rawTvShow).orElse(extractEndYear(rawTvShow))
} yield TvShow(title, startYear, endYear)

def extractTitle(rawTvShow: String): Either[String, String] =
  extractStringValue(rawTvShow, None, Some('(')) match {
    case Some(title) => Right(title.trim)
    case None =>
      Left(s"unable to parse the title from ${
          if (rawTvShow.length == 0) "empty text" else rawTvShow
        }")
  }

def extractStartYear(rawTvShow: String): Either[String, Int] =
  extractStringValue(rawTvShow, Some('('), Some('-')) match {
    case Some(startYearOption) =>
      startYearOption.toIntOption match {
        case Some(startYear) => Right(startYear)
        case None => Left(s"start year is not a valid year ${startYearOption}")
      }
    case None => Left(s"unable to parse the start year ${rawTvShow}")
  }

def extractEndYear(rawTvShow: String): Either[String, Int] =
  extractStringValue(rawTvShow, Some('-'), Some(')')) match {
    case Some(endYearOption) =>
      endYearOption.toIntOption match {
        case Some(endYear) => Right(endYear)
        case None => Left(s"end year is not a valid year ${endYearOption}")
      }
    case None => Left(s"unable to parse the end year ${rawTvShow}")
  }

def extractSingleYear(rawTvShow: String): Either[String, Int] =
  extractStringValue(rawTvShow, Some('('), Some(')')) match {
    case Some(yearOption) =>
      yearOption.toIntOption match {
        case Some(year) =>
          if (year > 0) Right(year)
          else Left(s"year is not a valid year ${year}")
        case None => Left(s"year is not a valid year ${yearOption}")
      }
    case None => Left(s"unable to parse the year ${rawTvShow}")
  }

def extractStringValue(
    text: String,
    startChar: Option[Char],
    endChar: Option[Char]
): Option[String] = {
  val startPos = startChar.map(text.indexOf(_) + 1).getOrElse(0)
  val endPos = endChar.map(text.indexOf(_)).getOrElse(text.length)
  if (endPos - startPos > 1) Some(text.substring(startPos, endPos)) else None
}
