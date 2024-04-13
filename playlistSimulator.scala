case class Playlist(
    name: String,
    kind: Kind,
    songs: List[Song],
    user: Option[User],
    artist: Option[Artist],
    genres: Option[Set[Genre]]
)

enum Kind(description: String) {
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
}
