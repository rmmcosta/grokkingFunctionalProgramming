import model._
import Genre._
import YearsActive._

object Main extends App {
  println(
    s"should print 2 alternative rock artists ${searchArtists(goodArtists, List(AlternativeRock))}"
  )
  println(
    s"should print 3 USA artists ${searchArtists(goodArtists, locations = List(Location("USA")))}"
  )
  println(
    s"should print 1 artist between 1980 and 1993 ${searchArtists(goodArtists, searchByActiveYears = true, activeSince = 1980, activeUntil = 1993)}"
  )
  println(
    s"should print 1 artist between 2007 and 2023 ${searchArtists(goodArtists, searchByActiveYears = true, activeSince = 2007, activeUntil = 2023)}"
  )
}

def goodArtists = List(
  Artist(
    "Linkin Park",
    AlternativeRock,
    Location("USA"),
    ActiveBetween(2002, 2019)
  ),
  Artist(
    "Led Zepplin",
    HardRock,
    Location("England"),
    ActiveBetween(1980, 1990)
  ),
  Artist(
    "Smashing Pumpkins",
    Rock,
    Location("USA"),
    ActiveBetween(1980, 1993)
  ),
  Artist(
    "30 Seconds to Mars",
    AlternativeRock,
    Location("USA"),
    StillActive(2007)
  )
)

def searchArtists(
    artists: List[Artist],
    genres: List[Genre] = List.empty,
    locations: List[Location] = List.empty,
    searchByActiveYears: Boolean = false,
    activeUntil: Int = 0,
    activeSince: Int = 0
): List[Artist] = {
  val filteredByGenre: List[Artist] =
    artists.filter(artist => genres.isEmpty || genres.contains(artist.genre))
  val filteredByLocation: List[Artist] = filteredByGenre.filter(artist =>
    locations.isEmpty || locations.contains(artist.origin)
  )
  if (searchByActiveYears)
    filterByYearsActive(filteredByLocation, activeSince, activeUntil)
  else
    filteredByLocation
}

def filterByYearsActive(
    artists: List[Artist],
    activeSince: Int,
    activeUntil: Int
) =
  artists.filter(artist =>
    artist.yearsActive match {
      case ActiveBetween(start, end) =>
        start <= activeSince && end >= activeUntil
      case StillActive(start) => start <= activeSince
    }
  )

case class Artist(
    name: String,
    genre: Genre,
    origin: Location,
    yearsActive: YearsActive
)

//we need to declare the opaque type inside another object because if the opaque type is in the same context as it's use it will allow strings and not
//obly us to use Location("something")
object model {
  opaque type Location = String

  object Location {
    def apply(value: String): Location = value
    extension (value: Location) def name: String = value
  }
}

enum Genre(val name: String) {
  case AlternativeRock extends Genre("Alternative Rock")
  case HardRock extends Genre("Hard Rock")
  case Rock extends Genre("Rock")
}

enum YearsActive {
  case StillActive(start: Int)
  case ActiveBetween(start: Int, end: Int)
}