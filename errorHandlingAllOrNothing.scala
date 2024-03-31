object main extends App {
  val tvShows = List(TvShow("Sopranos", 2018, 2023))
  println(tvShows)
  val updatedTvShows =
    addOrResign(Some(tvShows), Some(TvShow("Friends", 2000, 2009)))
  println(updatedTvShows)
  val updated2TvShows = addOrResign(updatedTvShows, None)
  println(updated2TvShows)
}

def addOrResign(
    parsedShows: Option[List[TvShow]],
    newShow: Option[TvShow]
): Option[List[TvShow]] = {
    newShow match {
        case Some(_) => Some(parsedShows.getOrElse(List()).appended(newShow.get))
        case None => parsedShows
    }
}

case class TvShow(
    title: String,
    startYear: Int,
    endYear: Int
)
