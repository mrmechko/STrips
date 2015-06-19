package com.github.mrmechko.strips

import com.github.mrmechko.swordnet.structures.{SSynset, SRelationType, SKey}

import scala.io.Source
import scala.xml.XML

/**
 * Created by mechko on 3/27/15.
 */
object THierarchy {
  def ensureLoaded : Boolean = {
    if (!LoadTrips.loaded)
      LoadTrips()
    LoadTrips.loaded
  }
  var _ancestor = Map[TConcept, TConcept]()
  var _children = Map[TConcept, Set[TConcept]]()
  var _toWordNet = Map[TConcept, Set[Int]]()
  var _fromWordNet = Map[Int, TConcept]()
  var _lexicon = Map[String, Set[TConcept]]()

  /**
   * returns a trips type for a wordnet synset.
   * @param key
   * @param blocked
   * @return
   */
  def fromWordNet(key : SKey, blocked : Seq[Int] = Seq()) : Seq[TConcept] = {
    var found : Seq[TConcept] = Seq()

    if (blocked.contains(key.synset)) {
      Seq()
    } else {
      _fromWordNet.get(key.offset) match {
        case Some(s) => Seq(s)
        case None => key.hasSemantic(SRelationType.hypernym).flatMap(s=>s.keys).map(h => fromWordNet(h, blocked.+:(key.offset))).distinct.flatten
      }
    }
  }

  def _init_children = {
    _children = _ancestor.toList.groupBy(_._2).mapValues(r => (r map {_._1}).toSet)
  }
}



object LoadTrips {
  

  private var _loaded : Boolean = false

  def normalizeSenseKey(key : String) : String = {
    /**
     * If the key does not contain enough :, add them to the end
     */

    var newKey = key

    while (newKey.count(_==':') < 4) newKey = newKey+":"

    newKey
  }

  def loaded = _loaded
  def apply(source : String = "/Users/mechko/nlpTools/flaming-tyrion/") = {
    _loaded = true
    val xml = XML.loadFile(source+"trips-ont-dsl.xml")

    //System.err.println("loading trips...")
    (xml \\ "dsl" \ "concept") foreach {concept =>
      //System.err.println((concept \ "@name").text.toLowerCase)
      val conceptName = TConcept((concept \ "@name").text.toLowerCase)
      //each concept has a set of relations, at most one inherit and several to workdnet
      var featsInherit = Seq[String]()

      // TODO: Figure out what to do with the simple template types (eg abstr-obj and the like)
      (concept \ "relation") foreach {relation =>

        val relName = (relation \ "@label").text.toLowerCase
        val relVals = (relation.text.toLowerCase.trim.split("\n").map(_ trim)) //List of strings containing the values.  For inherit there will be only one
        //System.err.printf("found %s of type %s\n", relVals.mkString("["," ,", "]"), relName)
        if(relName == "inherit"){
          //System.err.println("adding an inherit")
          featsInherit = featsInherit ++ relVals //All relvals now inherit
          relVals filter {rv=> rv startsWith "ont::"} foreach {m =>
            //System.err.printf("adding %s to %s\n", m, conceptName.name)
            THierarchy._ancestor = THierarchy._ancestor.updated(conceptName, TConcept(m.toLowerCase))
          }
        } else if (relName == "overlap") { //All wordnet concepts are of type overlap
          //Normalize the loaded key to head (?)
          // TODO: Verify this with either Will or James.  Some lexical information may be lost.
          val wn = relVals.filter(rv=> rv startsWith "wn::").map(m=>{
            SKey.get(normalizeSenseKey((m trim).stripPrefix("wn::|").stripSuffix("|") toLowerCase))
          }).collect{case Some(x) => x}
          wn foreach { rv =>
            THierarchy._fromWordNet = THierarchy._fromWordNet.updated(rv.offset, conceptName)
          }
          THierarchy._toWordNet = THierarchy._toWordNet.updated(conceptName, wn.map(_.offset).toSet)
        }
      }


      //Load The feats
      featsInherit = featsInherit ++ ((concept \ "sem-feats" \ "relation") filter {n=> (n \ "@label") == "inherit"} map (n => n.text.trim.toLowerCase.split("\n").map(_ trim))).flatten

      TFeatureTemplate.index = TFeatureTemplate.index.updated(conceptName.name, TFeatureTemplate(conceptName.name, (concept \ "sem-feats" \ "feat") map { f=>
        TFeatureTemplateElement(TFeatureType((f \ "@name").text.toLowerCase), TFeatureVal(f.text.trim.toLowerCase))
      }, featsInherit.distinct))
    }
    THierarchy._init_children
    THierarchy._lexicon = Source.fromFile(source+"lexicon.trips").getLines.flatMap(l => {
      val line = l.stripLineEnd.split("\t")
      line.tail.map(w => (line.head->w))
    }).toList.groupBy(g => g._2).mapValues(s => s.map(_._1).map(c => TConcept("ont::"+c)).toSet).withDefaultValue(Set())
  }


}

