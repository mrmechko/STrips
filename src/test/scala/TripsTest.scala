/*
import com.github.mrmechko.strips.{THierarchy, LoadTrips, TConcept}

import com.github.mrmechko.strips.simple.STripsQuery*/

import com.github.mrmechko.strips.model.{STripsWord, STripsOntItem, STripsOntology}
import org.scalatest.{FlatSpec, Matchers}
import play.api.libs.json.Json
import com.github.mrmechko.strips.json.Implicits._

import scala.util.Random

class DoesNotCrashTest extends FlatSpec with Matchers {

  val ont = STripsOntology.readTripsOntologyXML("/Users/mechko/nlpTools/flaming-tyrion/lexicon/lexicon/data/")

  "A STripsOntItem" should "be jsonifyable" in {
    ont.nodes.foreach { n =>
      val json = Json.toJson(n)
      val redeemed = Json.fromJson[STripsOntItem](json)

      redeemed.get.toString shouldBe n.toString
    }
  }

  "A STripsWord" should "be jsonifyable" in {
    ont.words.foreach { n =>
      val json = Json.toJson(n)
      val redeemed = Json.fromJson[STripsWord](json)
      redeemed.get.toString shouldBe n.toString
    }
  }


  "The Trips Hierarchy" should "be jsonifyable" in {
    val json = Json.toJson(ont)
    println("jsonified")
    val redeemed = Json.fromJson[STripsOntology](json)

    println("redeemed")
    ont.toString shouldBe redeemed.get.toString
  }
}

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