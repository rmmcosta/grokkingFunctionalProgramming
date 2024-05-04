import cats.effect.IO
import CastTheDie.castTheDieImpure

def castTheDieImpureClient:Int = castTheDieImpure()

def castTheDiePureClient:IO[Int] = IO.delay(castTheDieImpure()) 

def castTheDiePureClientSum:IO[Int] = for {
  die1 <- castTheDiePureClient
  die2 <- castTheDiePureClient
} yield die1 + die2
