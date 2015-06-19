package com.github.mrmechko.strips.simple

import com.github.mrmechko.strips.{THierarchy, TConcept}
import com.github.mrmechko.swordnet.SWordNet
import com.github.mrmechko.swordnet.structures.{SPos, SKey}

/**
 * Created by mechko on 6/19/15.
 */
object STripsQuery {

  def findWord(lemma : String) : Set[TConcept] = {
    SWordNet.l2S(lemma).map(key => {
      THierarchy.fromWordNet(key)
    }).flatten.distinct.toSet ++ THierarchy._lexicon(lemma)
  }

  def findWord(lemma : String, pos : SPos) : Set[TConcept] = {
    SWordNet.lp2S(lemma, pos).map(key => {
      THierarchy.fromWordNet(key)
    }).flatten.distinct.toSet ++ THierarchy._lexicon(lemma)
  }

  def pathToRoot(concept : TConcept) : Seq[TConcept] = {
    concept.ancestor match {
      case Some(a) => pathToRoot(a).+:(concept)
      case None => Seq(concept)
    }
  }

}
