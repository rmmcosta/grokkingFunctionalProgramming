import model._
import Genre._
import YearsActive._
import SearchCondition._

object Main extends App {
  println(
    s"should print 2 alternative rock artists ${searchArtists(goodArtists, SearchByGenre(List(AlternativeRock)))}"
  )
  println(
    s"should print 3 USA artists ${searchArtists(goodArtists, SearchByLocation(Location("USA")))}"
  )
  println(
    s"should print 1 artist between 1980 and 1993 ${searchArtists(goodArtists, SearchByActiveYears(1980, 1993))}"
  )
  println(
    s"should print 1 artist between 2007 and 2023 ${searchArtists(goodArtists, SearchByActiveYears(2007, 2023))}"
  )
  println(
    s"should print one Rock artist and one Hard Rock artist ${searchArtists(goodArtists, SearchByGenre(List(Rock, HardRock)))}"
  )
  println(
    s"should print 17 years: ${howLongWasArtistActive(goodArtists.apply(0), 2024)}"
  )
  println(
    s"should print 10 years: ${howLongWasArtistActive(goodArtists.apply(1), 2024)}"
  )
  println(
    s"should print 13 years: ${howLongWasArtistActive(goodArtists.apply(2), 2024)}"
  )
  println(
    s"should print 17 years: ${howLongWasArtistActive(goodArtists.apply(3), 2024)}"
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

def howLongWasArtistActive(artist: Artist, currentYear: Int): Int =
  artist.yearsActive match {
    case StillActive(start)        => currentYear - start
    case ActiveBetween(start, end) => end - start
  }

def searchArtists(
    artists: List[Artist],
    searchCondition: SearchCondition
): List[Artist] =
  artists.filter(artist =>
    searchCondition match {
      case SearchByGenre(genres)      => genres.contains(artist.genre)
      case SearchByLocation(location) => artist.origin == location
      case SearchByActiveYears(start, end) =>
        wasArtistActive(artist, start, end)
    }
  )

def wasArtistActive(
    artist: Artist,
    activeSince: Int,
    activeUntil: Int
): Boolean =
  artist.yearsActive match {
    case ActiveBetween(start, end) =>
      start <= activeSince && end >= activeUntil
    case StillActive(start) => start <= activeSince
  }

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

enum SearchCondition {
  case SearchByGenre(genres: List[Genre])
  case SearchByLocation(location: Location)
  case SearchByActiveYears(start: Int, end: Int)
}
