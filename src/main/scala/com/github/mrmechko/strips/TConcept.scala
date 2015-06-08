package com.github.mrmechko.trips

import com.github.mrmechko.swordnet.SKey

/**
 * Created by mechko on 6/1/15.
 */
case class TConcept(name : String) {
  THierarchy.ensureLoaded
  def ancestor : Option[TConcept] = THierarchy._ancestor.get(this)
  def children : Set[TConcept] = THierarchy._children.getOrElse(this, Set[TConcept]())
  def wordnetKeys : Set[SKey] = THierarchy._toWordNet.getOrElse(this, Set[SKey]())
  def features : Option[TFeatureTemplate] = TFeatureTemplate.get(name)
}
