package com.github.mrmechko.strips.model

import java.io.File

import com.github.mrmechko.swordnet.structures.SPos

import scala.xml.{NodeSeq, XML}


case class STripsOntology(version : String, nodes : List[STripsOntItem], words : List[STripsWord])

object STripsOntology extends App {

  def getListOfSubDirectories(directoryName: String, prefix : String): Array[String] = {
    return (new File(directoryName)).listFiles.filter(f => f.isFile && f.getName.startsWith(prefix)).map(_.getName)
  }

  readTripsOntologyXML("/Users/mechko/nlpTools/flaming-tyrion/lexicon/lexicon/data/").nodes.foreach(println)

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
  private def nodeSeq2FeatureTempl(n : NodeSeq) : SFeatureTemplate = SFeatureTemplate("empty", List(), Map())

  def readTripsOntologyXML(path2directory:String) : STripsOntology = {

    System.err.println("loading words...")
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

      println((f \\ "@name").text)
      val name = STripsOntName((f \\ "@name").text)

      val parent = (f \\ "@parent").text
      val semFeats = (f \\ "SEM")
      val features = nodeSeq2FeatureTempl(semFeats \ "FEATURES") // figure out how to parse this
      val frames = nodeSeq2Frame(f \ "ARGUMENT")
      //lexical items should get loaded from the preloaded thingy TODO
      val wordnet = (f \ "MAPPING").filter(m => (m \\ "@to").text == "wordnet").map(m => (m\\"@name").text).toList

      val wordList = ontname2word.getOrElse(name, List[STripsWord]())
      printf("found: %s\n\tchild-of: %s\n\twithSenses: %s\n\twithWords: %s\n-\n", name.id, parent, wordnet.mkString(", "), wordList.map(_.id).mkString(", "))
      STripsOntItem(name, wordList, wordnet, features, frames)
    }).toList

    STripsOntology("v1.0", ontItems, words)
  }

  def readTripsLexiconKeys(path2directory:String): List[STripsWord] = {
    getListOfSubDirectories(path2directory, "W::")/*.filter(_.toLowerCase().startsWith("w::a"))*/.flatMap(e => {
      val f = XML.loadFile(path2directory+e)
      val word = (f \\ "WORD" \ "@name").text
      //printf("W::%s\n", word)

      (f \ "POS").map(n => {
        val types = ((n \ "CLASS") \\ "@onttype").map(onttype => STripsOntName(onttype.text)).toList
        STripsWord(value = word, pos = SPos((n \\ "@name").text), ontTypes = types)
      }).map(x => {
        //println(x)
        x
      })
    }).toList
  }
}
