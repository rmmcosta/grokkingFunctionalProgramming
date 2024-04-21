def getValidTvShows(tvShows: List[String]): List[String] = {
  tvShows.flatMap(parseTvShow)//don't print nones
}

def parseTvShow(tvShow: String): Option[String] = {
  val openParenthesisPosition = tvShow.indexOf("(")
  val closeParenthesisPosition = tvShow.indexOf(")")
  if (openParenthesisPosition == -1 || closeParenthesisPosition == -1)
    None
  else if (closeParenthesisPosition - openParenthesisPosition <= 0)
    None
  else
    Some(
      tvShow.substring(openParenthesisPosition + 1, closeParenthesisPosition)
    )
}

object main extends App {
    val tvShows = List(
        "(Sopranos)",
        "Friends",
        "(How I met your mother)",
    )
    print(getValidTvShows(tvShows))
}