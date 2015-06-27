/*
import com.github.mrmechko.strips.{THierarchy, LoadTrips, TConcept}

import com.github.mrmechko.strips.simple.STripsQuery*/

import com.github.mrmechko.strips.model.{ STripsWord, STripsOntItem, STripsOntology, STripsOntName }
import com.github.mrmechko.swordnet.structures.SPos
import org.scalatest.{ FlatSpec, Matchers }
import play.api.libs.json.Json
import com.github.mrmechko.strips.json.Implicits._

import monocle.macros.{ GenLens, Lenses }

import scala.util.Random

class DoesNotCrashTest extends FlatSpec with Matchers {

  val ont = STripsOntology.readTripsOntologyXML()

  printf("there are %d nodes", ont.nodes.size)

  "A STripsOntItem" should "be jsonifyable" in {
    ont.nodes.foreach { n =>
      val json = Json.toJson(n)
      val redeemed = Json.fromJson[STripsOntItem](json).asOpt

      /*redeemed match {
        case Some(x) => printf("deserialized: %s\n", x.name.u)
        case None => {
          printf("failed: %s\n", n.name.u)
          println(Json.prettyPrint(json))
        }
      }*/
      redeemed.get.toString shouldBe n.toString
    }
  }

  "A STripsOntItem" should "have the correct features" in {
    println(ont.nodeByName(STripsOntName.build("accept")))
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

  "Lenses" should "be able to get and set OntItem fields" in {
    val n50 = ont.nodes(50)
    val n50n = n50.copy(gloss = "thing")

    STripsOntItem._gloss.get(n50) shouldBe ""
    STripsOntItem._gloss.set("thing")(n50) shouldBe n50n

  }

  "the word finder functions" should "work" in {
    ont.findWordClasses("cat")//.foreach(x => println(x.u))
    println("---")
    ont.findWordPosClasses("cat", SPos("noun"))//.foreach(x => println(x.u))
    println("---")
    ont.findSenseClasses("cat%1:05:00::")//.foreach(x => println(x.u))
    println("---")
    ont.findAllClasses("cat")//.foreach(x => println(x.u))
  }

  "the path to root functions" should "work" in {
    ont.findSenseClasses("cat%1:05:00::").map(ont.pathToRoot(_)).map(_.map(t => t.u))//.foreach(x => println(x.mkString("->")))
  }
}
