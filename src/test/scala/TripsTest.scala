/*
import com.github.mrmechko.strips.{THierarchy, LoadTrips, TConcept}

import com.github.mrmechko.strips.simple.STripsQuery
import org.scalatest.{FlatSpec, Matchers}
 */

/**
 * Created by mechko on 6/19/15.
 */
/**
class BaseTest extends FlatSpec with Matchers {
  def h(lemma : TConcept) = println(STripsQuery.pathToRoot(lemma).map(_.name).mkString("%s: [".format(lemma.name), ", ", "]"))
  def f(lemma : String) = println(STripsQuery.findWord(lemma).map(_.name).mkString("%s: [".format(lemma), ", ", "]"))
  def p(lemma : String) = println(STripsQuery.findWordBySense(lemma).map(r=> "sense:\n\t%s\ttrips\t%s".format(r._1.id, r._2.name)).mkString("%s:".format(lemma), "\n  ", "]"))

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

  "a trips search" should "be able to return sense pairs" in {
    p("cat")
    p("dog")
    p("run")
    p("fight")
    p("kill")
  }

  "a trips concept" should "head to the root" in {
    h(TConcept("ont::pass"))
  }
}
**/