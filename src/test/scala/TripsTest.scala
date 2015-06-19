import com.github.mrmechko.strips.{THierarchy, LoadTrips, TConcept}
import com.github.mrmechko.strips.simple.STripsQuery
import org.scalatest.{FlatSpec, Matchers}

/**
 * Created by mechko on 6/19/15.
 */
class BaseTest extends FlatSpec with Matchers {
  def f(lemma : String) = println(STripsQuery.findWord(lemma).map(_.name).mkString("%s: [".format(lemma), ", ", "]"))
  def h(lemma : TConcept) = println(STripsQuery.pathToRoot(lemma).map(_.name).mkString("%s: [".format(lemma.name), ", ", "]"))

  LoadTrips()

  "the trips hierarchy" should "load" in {
    THierarchy.ensureLoaded
    THierarchy._children.keys.foreach(println)
  }

  "a word" should "be searchable" in {
    f("cat")
    f("dog")
    f("run")
    f("fight")
    f("kill")
  }

  "a trips concept" should "head to the root" in {
    h(TConcept("ont::pass"))
  }
}
