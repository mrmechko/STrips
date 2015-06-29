/*
import com.github.mrmechko.strips.{THierarchy, LoadTrips, TConcept}

import com.github.mrmechko.strips.simple.STripsQuery*/

import com.github.mrmechko.strips.model._ //{ STripsWord, STripsOntItem, STripsOntology, STripsOntName, SFrame }
import com.github.mrmechko.strips.modify.{ReplaceGloss, ReplaceMultipleGlosses}
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
    val res = ont.nodes.foreach { n =>
      val json = Json.toJson(n)
      val redeemed = Json.fromJson[STripsOntItem](json).asOpt
      n shouldBe redeemed.get
    }
  }

  def testOntFrames( ontName : STripsOntName, frames : List[SFrame]) = ???

  "A STripsOntItem" should "have the correct features" in {
    val acc = ont.nodeByName(STripsOntName.build("accept"))

    val testFrame = SFrame("spatial-loc", true, "?!type", List())

    acc.frame.contains(testFrame) shouldBe true
  }

  "A STripsWord" should "be jsonifyable" in {
    ont.words.foreach { n =>
      val json = Json.toJson(n)
      val redeemed = Json.fromJson[STripsWord](json)
      (redeemed.get == n) shouldBe true
    }
  }

  "The Trips Hierarchy" should "be jsonifyable" in {
    val json = Json.toJson(ont)
    println("jsonified")

    val redeemed = Json.fromJson[STripsOntology](json)
    println("redeemed")

    import java.io._
    val pw = new PrintWriter(new File("ont_test.json"))
    pw.write(Json.prettyPrint(json))
    pw.close

    (ont == redeemed.get) shouldBe true
  }

  "Lenses" should "be able to get and set OntItem fields" in {
    val n50 = ont.nodes(50)
    val n50n = n50.copy(gloss = "thing")

    STripsOntItem._gloss.get(n50) shouldBe ""
    STripsOntItem._gloss.set("thing")(n50) shouldBe n50n
    STripsOntItem._gloss.set("thing")(n50).gloss shouldBe "thing"

  }

  "A gloss" should "be replaceable" in {
    val newOnt = ReplaceGloss(STripsOntName.build("bread"), "food made from dough of flour or meal and usually raised with yeast or baking powder and then baked")(ont)

    val json = Json.toJson(newOnt)
    import java.io._
    val pw = new PrintWriter(new File("ont_test_replace_gloss.json"))
    pw.write(Json.prettyPrint(json))
    pw.close
  }

  "A gloss collection" should "be writeable" in {
    val rep1 = ReplaceGloss(STripsOntName.build("bread"), "food made from dough of flour or meal and usually raised with yeast or baking powder and then baked")

    val rep2 = ReplaceGloss(STripsOntName.build("vehicle"), "a vehicle")

    val repset = ReplaceMultipleGlosses(List(rep1, rep2))

    val newOnt = repset(ont)

    val json = Json.toJson(newOnt)
    import java.io._
    val pw = new PrintWriter(new File("ont_test_replace_multiple_gloss.json"))
    pw.write(Json.prettyPrint(json))
    pw.close
  }

  "A ReplaceGloss" should "be (de)serializeable" in {
    val rep = ReplaceGloss(STripsOntName.build("bread"), "food made from dough of flour or meal and usually raised with yeast or baking powder and then baked")

    import com.github.mrmechko.strips.json.ModImplicits._

    val json = Json.toJson(rep)
    println(Json.prettyPrint(json))
    val redeemed = Json.fromJson[ReplaceGloss](json)

    redeemed.get shouldBe rep
  }

  "A ReplaceMultipleGlosses" should "be (de)serializeable" in {
    val rep1 = ReplaceGloss(STripsOntName.build("bread"), "food made from dough of flour or meal and usually raised with yeast or baking powder and then baked")

    val rep2 = ReplaceGloss(STripsOntName.build("vehicle"), "a vehicle")

    val repset = ReplaceMultipleGlosses(List(rep1, rep2))

    import com.github.mrmechko.strips.json.ModImplicits._

    val json = Json.toJson(repset)
    println(Json.prettyPrint(json))
    val redeemed = Json.fromJson[ReplaceMultipleGlosses](json)

    redeemed.get shouldBe repset
  }

  "the word finder functions" should "work" in {
    ont.findWordClasses("cat") //.foreach(x => println(x.u))
    println("---")
    ont.findWordPosClasses("cat", SPos("noun")) //.foreach(x => println(x.u))
    println("---")
    ont.findSenseClasses("cat%1:05:00::") //.foreach(x => println(x.u))
    println("---")
    ont.findAllClasses("cat") //.foreach(x => println(x.u))
  }

  "the path to root functions" should "work" in {
    ont.findSenseClasses("cat%1:05:00::").map(ont.pathToRoot(_)).map(_.map(t => t.u)) //.foreach(x => println(x.mkString("->")))
  }
}
