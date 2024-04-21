object Main extends App {
  println(
    s"should print 2 alternative rock artists ${searchArtists(goodArtists, List("Alternative Rock"))}"
  )
  println(
    s"should print 3 USA artists ${searchArtists(goodArtists, locations = List("USA"))}"
  )
  println(
    s"should print 1 artist between 1980 and 1993 ${searchArtists(goodArtists, searchByActiveYears = true, activeSince = 1980, activeUntil = 1993)}"
  )
  println(
    s"should print 1 artist between 2007 and 2023 ${searchArtists(goodArtists, searchByActiveYears = true, activeSince = 2007, activeUntil = 2023)}"
  )
}

def goodArtists = List(
  Artist("Linkin Park", "Alternative Rock", "USA", 2002, false, 2019),
  Artist("Led Zepplin", "Hard Rock", "ENGLAND", 1980, false, 1990),
  Artist("Smashing Pumpkins", "Rock", "USA", 1980, false, 1993),
  Artist("30 Seconds to Mars", "Alternative Rock", "USA", 2007, true, 0)
)

def badArtists = List(
  Artist("Wrong Genre", "xpto", "USA", 2002, false, 2019),
  Artist("Bad Origin", "Hard Rock", "Germany", 1980, false, 1990),
  Artist("Negative Start", "Alternative Rock", "USA", -2007, true, 0),
  Artist("End badly", "Alternative Rock", "USA", -2007, true, 2009),
  Artist("Active Wrong", "Alternative Rock", "USA", -2007, true, 2009)
)

def searchArtists(
    artists: List[Artist],
    genres: List[String] = List.empty,
    locations: List[String] = List.empty,
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
      artist.yearsActiveStart <= activeSince && (artist.isActive || artist.yearsActiveEnd >= activeUntil)
    )
  else
    filteredByLocation
}

case class Artist(
    name: String,
    genre: String,
    origin: String,
    yearsActiveStart: Int,
    isActive: Boolean,
    yearsActiveEnd: Int
)
