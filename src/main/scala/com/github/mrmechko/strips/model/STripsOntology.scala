package com.github.mrmechko.strips.model

import java.io.File

import com.github.mrmechko.swordnet.SWordNet
import com.github.mrmechko.swordnet.structures.{SRelationType, SKey, SPos}
import com.typesafe.config.ConfigFactory

import scala.xml.{NodeSeq, XML}

import monocle.macros.{GenLens, Lenses}


// STripsFeatureTemplate should be a list in STripsOntology rather than elements of STripsOntology
// This is to facilitate easy modification of the elements and easy searching.
@Lenses("_") case class STripsOntology(
  version : String,
  nodes : List[STripsOntItem],
  words : List[STripsWord],
  inheritance : Map[STripsOntName, STripsOntName]
  ) {
  lazy val wordMap = words.groupBy(x => x.value)
  lazy val wordPosMap = words.groupBy(x => (x.value, x.pos))

  lazy val nameToIndex = nodes.zipWithIndex.map(n => n._1.name -> n._2).toMap
  def nodeByName(name : STripsOntName) : STripsOntItem = nodes(nameToIndex(name))
  def nodeByNameOpt(name : STripsOntName) : Option[STripsOntItem] = nameToIndex.get(name).map(nodes(_))

  lazy val senseMap = nodes.flatMap(n => n.wordnetKeys.map(k => k->n)).groupBy(p => p._1).mapValues(n=>n.map(x => x._2.name))

  def pathToRoot(ontName : STripsOntName) : List[STripsOntName] = {
    inheritance.get(ontName) match {
      case Some(x) => pathToRoot(x).+:(ontName)
      case None => List(ontName)
    }
  }

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

  def findAllClasses(lemma : String) : List[STripsOntName] = {
    findAllSenseClasses(lemma)++(findWordClasses(lemma)).distinct
  }

  def findAllSenseClasses(lemma : String) : List[STripsOntName] = {
    SWordNet.l2S(lemma).flatMap(k => findSenseClasses(k.key)).distinct.toList
  }

  def findClassBySense(lemma : String) : List[(String, STripsOntName)] ={
    List()
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

  def findOntByString(ont : String) : Option[STripsOntItem]  = {
    nodeByNameOpt(STripsOntName.build(ont))
  }

}

object STripsOntology {

  val defaultPath : String = ConfigFactory.load().getString("strips.XMLSource")

  def getListOfSubDirectories(directoryName: String, prefix : String): Array[String] = {
    return (new File(directoryName)).listFiles.filter(f => f.isFile && f.getName.startsWith(prefix)).map(_.getName)
  }


  // TODO : Try each possible feature type safely
  private def nodeSeq2Features(n : NodeSeq) : Map[SFeatureType, SFeatureVal] = {
    SFeatureType.features.map(f => {
      f -> (n \@ f.name)//.text
    }).filter(_._2 != "").map(x => (x._1 -> SFeatureVal(x._2))).toMap
  }
  private def nodeSeq2Frame(n : NodeSeq) : List[SFrame] = {
    n.map(m => {
      val role = (m \\ "@role").text
      val optionality = (m \\ "@optionality").text == "optional"
      val fltype = (m \\ "@fltype").text
      val feats = (m \ "FEATURES")
      SFrame(role, optionality, fltype, List())
    }).toList
  }
  private def nodeSeq2FeatureTempl(name : String, n : NodeSeq) : SFeatureTemplate =  {

    SFeatureTemplate.build(name, (n \@ "fltype"), nodeSeq2Features(n \\ "FEATURES"))
  }

  def readTripsOntologyXML(path2directory:String = defaultPath) : STripsOntology = {

    val words = readTripsLexiconKeys(path2directory)
    val ontname2word = words.flatMap(x => x.ontTypes.map(y => y -> x)).groupBy(v => v._1).mapValues(x=>x.map(_._2))
    System.err.println("loading ontItems...")
    val ontItems = getListOfSubDirectories(path2directory, "ONT_")/*.filter(_.toLowerCase().startsWith("ont::a"))*/.map(e => {
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
      val features = nodeSeq2FeatureTempl(name.name, semFeats) // figure out how to parse this. Should take the whole sem object
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
    getListOfSubDirectories(path2directory, "W_")/*.filter(_.toLowerCase().startsWith("w::a"))*/.flatMap(e => {
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
