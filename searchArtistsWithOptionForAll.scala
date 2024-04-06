import model._
import Genre._

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
  Artist("Linkin Park", AlternativeRock, Location("USA"), 2002, Some(2019)),
  Artist("Led Zepplin", HardRock, Location("England"), 1980, Some(1990)),
  Artist("Smashing Pumpkins", Rock, Location("USA"), 1980, Some(1993)),
  Artist("30 Seconds to Mars", AlternativeRock, Location("USA"), 2007)
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
    filteredByLocation.filter(artist =>
      artist.yearsActiveStart <= activeSince && (artist.yearsActiveEnd
        .forall(_ >= activeUntil))
    )
  else
    filteredByLocation
}

case class Artist(
    name: String,
    genre: Genre,
    origin: Location,
    yearsActiveStart: Int,
    yearsActiveEnd: Option[Int] = None
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
