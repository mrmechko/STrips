package com.github.mrmechko.strips.simple
/**
import com.github.mrmechko.strips.{THierarchy, TConcept}
import com.github.mrmechko.swordnet.SWordNet
import com.github.mrmechko.swordnet.structures.{SPos, SKey}

object STripsQuery {

  def findWord(lemma : String) : Set[TConcept] = {
    SWordNet.l2S(lemma).map(key => {
      THierarchy.fromWordNet(key)
    }).flatten.distinct.toSet ++ THierarchy._lexicon(lemma)
  }

  def fromLexicon(lemma : String) : Set[TConcept] = THierarchy._lexicon(lemma)

  def findWordBySense(lemma : String) : Set[(SKey, TConcept)] = {
    SWordNet.l2S(lemma).map(key => {
      THierarchy.fromWordNet(key).map(t => (key,t))
    }).flatten.toSet
  }

  def findWord(lemma : String, pos : SPos) : Set[TConcept] = {
    SWordNet.lp2S(lemma, pos).map(key => {
      THierarchy.fromWordNet(key)
    }).flatten.distinct.toSet ++ THierarchy._lexicon(lemma)
  }

  def findWordBySense(lemma : String, pos : SPos) : Set[(SKey, TConcept)] = {
    SWordNet.lp2S(lemma, pos).map(key => {
      THierarchy.fromWordNet(key).map(t => (key,t))
    }).flatten.toSet
  }

  def pathToRoot(concept : TConcept) : Seq[TConcept] = {
    concept.ancestor match {
      case Some(a) => pathToRoot(a).+:(concept)
      case None => Seq(concept)
    }
  }

}
**/