object Main extends App {
  println(
    s"should print users that haven't specified their city or live in Melbourne - ${getUsersByCityOrNoCity("Melbourne")}"
  )
  println(
    s"should print users that live in Lagos - ${getUsersByCity("Lagos")}"
  )
  println(
    s"should print users that like Bee Gees - ${getUsersByFavoriteArtist("Bee Gees")}"
  )
  println(
    s"should print users that live in cities that start with the letter T - ${getUsersByCityStartingLetter('T')}"
  )
  println(
    s"should print users that only like artists that have a name longer than 8 characters (or no favorite artists at all) - ${getUsersByArtistsNameLongerThanOrNone(8)}"
  )
  println(
    s"should print users that like some artists whose names start with an M - ${getUsersByArtistsStartLetter('M')}"
  )
}

val users = List(
  User("Alice", Some("Melbourne"), List("Bee Gees")),
  User("Bob", Some("Lagos"), List("Bee Gees")),
  User("Eve", Some("Tokyo"), List.empty),
  User("Mallory", favoriteArtists = List("Metallica", "Bee Gees")),
  User("Trent", Some("Buenos Aires"), List("Led Zeppelin"))
)

case class User(
    name: String,
    city: Option[String] = None,
    favoriteArtists: List[String]
)

def getUsersByCityOrNoCity(city: String): List[User] =
  users.filter(user => user.city.forall(_ == city))

def getUsersByCity(city: String): List[User] =
  users.filter(user => user.city.exists(_ == city))

def getUsersByFavoriteArtist(favoriteArtist: String): List[User] =
  users.filter(user => user.favoriteArtists.contains(favoriteArtist))

def getUsersByCityStartingLetter(startingLetter: Char) =
  users.filter(user => user.city.getOrElse("0").head == startingLetter)

def getUsersByArtistsNameLongerThanOrNone(nameLength: Int) =
  users.filter(user => user.favoriteArtists.forall(_.length > nameLength))

def getUsersByArtistsStartLetter(startLetter: Char) = users.filter(user =>
  hasOneValueStartingWithLetter(user.favoriteArtists, startLetter)
)

def hasOneValueStartingWithLetter(values: List[String], letter: Char): Boolean =
  values.map(_.head).contains(letter)
