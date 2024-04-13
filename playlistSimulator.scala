/*
    Here are few examples of playlists to test:
 A playlist named “This is Foo Fighters,” which is a playlist based on a
particular artist (Foo Fighters) with two songs: “Breakout” and
“Learn To Fly.”
 A playlist named “Deep Focus,” which is a playlist based on two genres
(funk and house) and has three songs: “One More Time” by Daft Punk and
“Hey Boy Hey Girl” by The Chemical Brothers.
 A playlist named “<Your Name>’s playlist,” which is a user-based playlist.
 */
import Kind._
import Genre._

object Main extends App {
  val examplePlaylist = List(
    thisIsFooFighters,
    deepFocus,
    myPlaylist
  )
//  println(examplePlaylist)
  examplePlaylist.foreach(_.print)
}

val thisIsFooFighters = Playlist(
  "This is Foo Fighters",
  BY_ARTIST,
  List(breakoutSong, learnToFlySong),
  artist = Some(fooFighters)
)
val deepFocus = Playlist(
  "Deep Focus",
  BY_GENRE,
  List(oneMoreTimeSong, heyBoyHeyGirlSong),
  genres = Some(Set(FUNK, HOUSE))
)
val myPlaylist = Playlist(
  "My Playlist",
  BY_USER,
  List(inTheEndSong, stairwayToHeavenSong, inTooDeepSong, californicationSong),
  user = Some(User("Ricardo Costa"))
)

def fooFighters = Artist("Foo Fighters")
def daftPunk = Artist("Daft Punk")
def chemicalBrothers = Artist("The Chemical Brothers")
def linkinPark = Artist("Linkin Park")
def ledZepplin = Artist("Led Zepplin")
def sum41 = Artist("Sum 41")
def redHotChiliPeppers = Artist("Red Hot Chili Peppers")

def breakoutSong = Song("Breakout", fooFighters)
def learnToFlySong = Song("Learn To Fly", fooFighters)
def oneMoreTimeSong = Song("One More Time", daftPunk)
def heyBoyHeyGirlSong = Song("Hey Boy Hey Girl", chemicalBrothers)
def inTheEndSong = Song("In The End", linkinPark)
def stairwayToHeavenSong = Song("Stairway To Heaven", ledZepplin)
def inTooDeepSong = Song("In Too Deep", sum41)
def californicationSong = Song("Californication", redHotChiliPeppers)

case class Playlist(
    name: String,
    kind: Kind,
    songs: List[Song],
    user: Option[User] = None,
    artist: Option[Artist] = None,
    genres: Option[Set[Genre]] = None
) {
  def print = println(s"""
        ${name} (${kind.description})
        ${printSongs}
        ${printUserArtistOrGenre}
    """)
  def printSongs = s"""songs: ${songs.map(printSong)} """
  def printSong(song: Song) = if (song != null)
    s"""${song.name} (${song.artist.name})"""
  else "invalid song"
  def printUserArtistOrGenre =
    (user, artist, genres) match {
      case (Some(user), None, None)   => s"user: ${user}"
      case (None, Some(artist), None) => s"artist: ${artist.name}"
      case (None, None, Some(genres)) => s"genres: ${genres}"
      case _                          => "invalid data"
    }
}

enum Kind(val description: String) {
  case BY_USER extends Kind("curated by a User")
  case BY_ARTIST extends Kind("based on a particular Artist")
  case BY_GENRE extends Kind("based on a specific set of Genres")
}

case class Song(
    name: String,
    artist: Artist
)

case class Artist(
    name: String
)

case class User(
    name: String
)

enum Genre {
  case ROCK
  case POP
  case POPULAR
  case FUNK
  case HOUSE
}
