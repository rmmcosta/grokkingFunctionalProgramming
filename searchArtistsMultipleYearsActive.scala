import model._
import Genre._
import YearsActive._
import SearchCondition._

object Main extends App {
  println(
    s"should print 2 alternative rock artists ${searchArtists(goodArtists, List(SearchByGenre(List(AlternativeRock))))}"
  )
  println(
    s"should print 3 USA artists ${searchArtists(goodArtists, List(SearchByLocation(List(Location("USA")))))}"
  )
  println(
    s"should print 1 artist between 1980 and 1993 ${searchArtists(goodArtists, List(SearchByActiveYears(1980, 1993)))}"
  )
  println(
    s"should print 1 artist between 2007 and 2023 ${searchArtists(goodArtists, List(SearchByActiveYears(2007, 2023)))}"
  )
  println(
    s"should print one Rock artist and one Hard Rock artist ${searchArtists(goodArtists, List(SearchByGenre(List(Rock, HardRock))))}"
  )
  println(
    s"should print all artists ${searchArtists(goodArtists, List.empty)}"
  )
  println(
    s"should print Carlos Paião since is the one active between 1945 and 1947 ${searchArtists(goodArtists, List(SearchByActiveYears(1945, 1947)))}"
  )
  println(
    s"should print Xutos e Pontapés since is the one active between 1975 and 1990 and since 2020 ${searchArtists(goodArtists, List(SearchByActiveYears(1975, 1990), SearchByActiveYears(2020, 2024)))}"
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
  println(
    s"should print 13 years: ${howLongWasArtistActive(goodArtists.apply(4), 2024)}"
  )
}

def goodArtists = List(
  Artist(
    "Linkin Park",
    AlternativeRock,
    Location("USA"),
    List(ActiveBetween(2002, 2019))
  ),
  Artist(
    "Led Zepplin",
    HardRock,
    Location("England"),
    List(ActiveBetween(1980, 1990))
  ),
  Artist(
    "Smashing Pumpkins",
    Rock,
    Location("USA"),
    List(ActiveBetween(1980, 1993))
  ),
  Artist(
    "30 Seconds to Mars",
    AlternativeRock,
    Location("USA"),
    List(StillActive(2007))
  ),
  Artist(
    "Carlos Paião",
    Popular,
    Location("Portugal"),
    List(ActiveBetween(1943, 1950), ActiveBetween(1954, 1958))
  ),
  Artist(
    "Xutos e Pontapés",
    Popular,
    Location("Portugal"),
    List(ActiveBetween(1975, 1990), StillActive(2020))
  )
)

def howLongWasArtistActive(artist: Artist, currentYear: Int): Int = {
  val activePeriods: List[Int] =
    artist.yearsActive.map(yearsActive =>
      yearsActive match {
        case StillActive(start)        => currentYear - start
        case ActiveBetween(start, end) => end - start
      }
    )
  activePeriods.sum
}

def searchArtists(
    artists: List[Artist],
    searchConditions: List[SearchCondition]
): List[Artist] =
  artists.filter(artist =>
    searchConditions.forall(passesSearchCondition(artist, _))
  )

def passesSearchCondition(
    artist: Artist,
    searchCondition: SearchCondition
): Boolean = searchCondition match {
  case SearchByGenre(genres)       => genres.contains(artist.genre)
  case SearchByLocation(locations) => locations.contains(artist.origin)
  case SearchByActiveYears(start, end) =>
    wasArtistActive(artist, start, end)
}

def wasArtistActive(
    artist: Artist,
    activeSince: Int,
    activeUntil: Int
): Boolean =
  artist.yearsActive.exists(yearsActive =>
    yearsActive match {
      case ActiveBetween(start, end) =>
        start <= activeSince && end >= activeUntil
      case StillActive(start) => start <= activeSince
    }
  )

case class Artist(
    name: String,
    genre: Genre,
    origin: Location,
    yearsActive: List[YearsActive]
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
  case Popular extends Genre("Popular")
}

enum YearsActive {
  case StillActive(start: Int)
  case ActiveBetween(start: Int, end: Int)
}

enum SearchCondition {
  case SearchByGenre(genres: List[Genre])
  case SearchByLocation(locations: List[Location])
  case SearchByActiveYears(start: Int, end: Int)
}
