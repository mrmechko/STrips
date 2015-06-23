package com.github.mrmechko.strips.model

import java.io.File

import com.github.mrmechko.swordnet.structures.{SRelationType, SKey, SPos}
import com.typesafe.config.ConfigFactory

import scala.xml.{NodeSeq, XML}


case class STripsOntology(version : String, nodes : List[STripsOntItem], words : List[STripsWord], inheritance : Map[STripsOntName, STripsOntName]) {
  lazy val wordMap = words.groupBy(x => x.value)
  lazy val wordPosMap = words.groupBy(x => (x.value, x.pos))

  lazy val nodeByName = nodes.map(n => n.name -> n).toMap

  lazy val senseMap = nodes.flatMap(n => n.wordnetKeys.map(k => k->n)).groupBy(p => p._1).mapValues(n=>n.map(x => x._2.name))

  def findWordClasses(lemma : String) : List[STripsOntName] = {
    wordMap.get(lemma) match {
      case Some(x) => x.flatMap(_.ontTypes).distinct
      case None => List()
    }
  }

  def findWordPosClasses(lemma : String, pos : SPos) : List[STripsOntName] = {
    wordPosMap.get((lemma, pos)) match {
      case Some(x) => x.flatMap(_.ontTypes).distinct
      case None => List()
    }
  }

  def findSenseClasses(sense : String, ignore : Set[String] = Set()) : List[STripsOntName] = {
    if(ignore.contains(sense)) List()
    else {
      senseMap.get(sense) match {
        case Some(x) => x
        case None => {
          SKey(sense).hasSemantic(SRelationType.hypernym).flatMap(_.keys).distinct.flatMap(k => findSenseClasses(k.key, ignore.+(sense))).distinct.toList
        }
      }
    }
  }

}

object STripsOntology {

  val defaultPath : String = ConfigFactory.load().getString("strips.XMLSource")

  def getListOfSubDirectories(directoryName: String, prefix : String): Array[String] = {
    return (new File(directoryName)).listFiles.filter(f => f.isFile && f.getName.startsWith(prefix)).map(_.getName)
  }


  private def nodeSeq2Features(n : NodeSeq) : List[(SFeatureType, SFeatureVal)] = List()
  private def nodeSeq2Frame(n : NodeSeq) : List[SFrame] = {
    n.map(m => {
      val role = (m \\ "@role").text
      val optionality = (m \\ "@optionality").text == "optional"
      val fltype = (m \\ "@fltype").text
      val feats = (m \ "FEATURES")
      SFrame(role, optionality, fltype, List())
    }).toList
  }
  private def nodeSeq2FeatureTempl(n : NodeSeq) : SFeatureTemplate = SFeatureTemplate.build("empty", List(), Map())

  def readTripsOntologyXML(path2directory:String = defaultPath) : STripsOntology = {

    val words = readTripsLexiconKeys(path2directory)
    val ontname2word = words.flatMap(x => x.ontTypes.map(y => y -> x)).groupBy(v => v._1).mapValues(x=>x.map(_._2))
    System.err.println("loading ontItems...")
    val ontItems = getListOfSubDirectories(path2directory, "ONT::")/*.filter(_.toLowerCase().startsWith("ont::a"))*/.map(e => {
      val f = XML.loadFile(path2directory+e) \\ "ONTTYPE"
      /**
       * id           : String,
       * name         : STripsOntName,
       * lexicalItems : List[STripsWord],
       * wordnetKeys  : List[String],
       * features     : SFeatureTemplate,
       * frame        : List[SFrame]
       **/


      val name = STripsOntName.build(f.\@("name"))

      val parent = STripsOntName.build(f \@ "parent")

      val semFeats = (f \\ "SEM")
      val features = nodeSeq2FeatureTempl(semFeats \ "FEATURES") // figure out how to parse this
      val frames = nodeSeq2Frame(f \ "ARGUMENT")

      val wordnet = (f \ "MAPPING").filter(m => (m \@ "to") == "wordnet").map(_ \@ "name").toList

      val wordList = ontname2word.getOrElse(name, List[STripsWord]())
      //printf("found: %s\n\tchild-of: %s\n\twithSenses: %s\n\twithWords: %s\n-\n", name.id, parent, wordnet.mkString(", "), wordList.map(_.id).mkString(", "))
      (STripsOntItem.build(name, wordList, wordnet, features, frames), parent)
    }).toList

    STripsOntology("v1.0", ontItems.map(_._1), words, ontItems.map(x => x._1.name -> x._2).toMap)
  }

  def readTripsLexiconKeys(path2directory:String = defaultPath): List[STripsWord] = {
    System.err.println("loading words from "+path2directory)
    getListOfSubDirectories(path2directory, "W::")/*.filter(_.toLowerCase().startsWith("w::a"))*/.flatMap(e => {
      val f = XML.loadFile(path2directory+e)
      val word = (f \\ "WORD" \ "@name").text
      //printf("W::%s\n", word)

      (f \ "POS").map(n => {
        val types = ((n \ "CLASS") \\ "@onttype").map(onttype => STripsOntName.build(onttype.text)).toList
        STripsWord.build(value = word, pos = SPos((n \\ "@name").text), ontTypes = types)
      }).map(x => {
        //println(x)
        x
      })
    }).toList
  }
}
