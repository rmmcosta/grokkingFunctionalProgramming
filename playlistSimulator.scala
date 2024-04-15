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
    deepFocus2,
    myPlaylist
  )
//  println(examplePlaylist)
  examplePlaylist.foreach(_.print)
  println(
    s"should print a list with 4 songs from Foo Fighters: ${gatherSongsByArtist(examplePlaylist, fooFighters)}"
  )
  println(
    s"should print Linkin Park + House songs: ${gatherSongsByArtistAndGenre(examplePlaylist, linkinPark, HOUSE)}"
  )
}

val thisIsFooFighters = Playlist(
  "This is Foo Fighters",
  BY_ARTIST(fooFighters),
  List(breakoutSong, learnToFlySong)
)
val deepFocus = Playlist(
  "Deep Focus",
  BY_GENRE(Set(FUNK, HOUSE)),
  List(oneMoreTimeSong, heyBoyHeyGirlSong, thePretenderSong)
)
val myPlaylist = Playlist(
  "My Playlist",
  BY_USER(User("Ricardo Costa")),
  List(
    inTheEndSong,
    stairwayToHeavenSong,
    inTooDeepSong,
    californicationSong,
    bestOfYouSong
  )
)
val deepFocus2 = Playlist(
  "Deep Focus 2",
  BY_GENRE(Set(HOUSE)),
  List(dontBeShy)
)

def fooFighters = Artist("Foo Fighters")
def daftPunk = Artist("Daft Punk")
def chemicalBrothers = Artist("The Chemical Brothers")
def linkinPark = Artist("Linkin Park")
def ledZepplin = Artist("Led Zepplin")
def sum41 = Artist("Sum 41")
def redHotChiliPeppers = Artist("Red Hot Chili Peppers")
def tiesto = Artist("Tiesto")

def dontBeShy = Song("Don't be Shy", tiesto)
def breakoutSong = Song("Breakout", fooFighters)
def learnToFlySong = Song("Learn To Fly", fooFighters)
def bestOfYouSong = Song("Best Of You", fooFighters)
def thePretenderSong = Song("The Pretender", fooFighters)
def oneMoreTimeSong = Song("One More Time", daftPunk)
def heyBoyHeyGirlSong = Song("Hey Boy Hey Girl", chemicalBrothers)
def inTheEndSong = Song("In The End", linkinPark)
def stairwayToHeavenSong = Song("Stairway To Heaven", ledZepplin)
def inTooDeepSong = Song("In Too Deep", sum41)
def californicationSong = Song("Californication", redHotChiliPeppers)

def gatherSongsByArtist(
    playlists: List[Playlist],
    artist: Artist
): List[Song] = playlists
  .flatMap(playlist => playlist.songs)
  .filter(song => song.artist == artist)

def gatherSongsByArtistAndGenre(
    playlists: List[Playlist],
    artist: Artist,
    genre: Genre
): List[Song] =
  playlists.foldLeft(List.empty)((songs, playlist) =>
    playlist.kind match {
      case BY_ARTIST(currentArtist) =>
        if (currentArtist == artist) songs.appendedAll(playlist.songs)
        else songs.appendedAll(List.empty)
      case BY_GENRE(currentGenres) =>
        if (currentGenres.contains(genre)) songs.appendedAll(playlist.songs)
        else songs.appendedAll(List.empty)
      case BY_USER(user) =>
        songs.appendedAll(playlist.songs.filter(song => song.artist == artist))
    }
  )

case class Playlist(
    name: String,
    kind: Kind,
    songs: List[Song]
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
    kind match {
      case BY_USER(user)     => s"user: ${user}"
      case BY_ARTIST(artist) => s"artist: ${artist.name}"
      case BY_GENRE(genres)  => s"genres: ${genres}"
    }
}

enum Kind(val description: String) {
  case BY_USER(user: User) extends Kind("curated by a User")
  case BY_ARTIST(artist: Artist) extends Kind("based on a particular Artist")
  case BY_GENRE(genres: Set[Genre])
      extends Kind("based on a specific set of Genres")
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
