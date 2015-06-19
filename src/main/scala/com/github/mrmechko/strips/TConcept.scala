package com.github.mrmechko.strips

import com.github.mrmechko.swordnet.structures.{SSynset, SKey}


/**
 * Created by mechko on 6/1/15.
 */
case class TConcept(name : String) {
  THierarchy.ensureLoaded
  def ancestor : Option[TConcept] = THierarchy._ancestor.get(this)
  def children : Set[TConcept] = THierarchy._children.getOrElse(this, Set[TConcept]())
  def wordnetKeys : Set[SKey] = THierarchy._toWordNet.get(this) match {
    case Some(set) => set.map(SSynset.get(_).map(_.keys)).collect{case Some(x) => x}.flatten
    case None => Set[SKey]()
  }
  def features : Option[TFeatureTemplate] = TFeatureTemplate.get(name)
}
