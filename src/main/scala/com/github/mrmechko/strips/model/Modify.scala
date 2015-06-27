package com.github.mrmechko.strips.model
import monocle.macros.{GenLens, Lenses}

trait ModifyOnt {
  def apply(ont: STripsOntology): STripsOntology
  def isValidOn(ont: STripsOntology): Boolean
  def canCommuteWith(other: ModifyOnt): Boolean
  def isIndependentOf(other: ModifyOnt): Boolean
  def affects(ont: STripsOntology): List[STripsOntItem]
}

/**
 * A single proposal that requires multiple basic operations
 */
case class ComplexModify(mods: IndexedSeq[ModifyOnt])
/**
 * Easy. T is Word, Frame, Feature, etc
 * Types are:
 * List Types: Example, WordNetKey, STripsWord, Frame
 * Element Types: FeatureTemplate, Gloss, Name
 */
case class AddElement[T](node: STripsOntItem, element: T)
case class RemElement[T](node: STripsOntItem, element: T)

object ModifyElement {
  val examples = STripsOntItem._examples
  val lexicalItems = STripsOntItem._lexicalItems
  val wordnetKeys = STripsOntItem._wordnetKeys
  val frames = STripsOntItem._frame

  val gloss = STripsOntItem._gloss
  val featureTemplate = STripsOntItem._features
  val name = STripsOntItem._name
}

/**
 * Easy
 *
 * version : String,
 * nodes : List[STripsOntItem],
 * words : List[STripsWord],
 * inheritance : Map[STripsOntName, STripsOntName] //Child->Parent
 */
case class AddNode(node: STripsOntItem, parent: STripsOntItem)

/**
 * 1. node gets children
 * 2. parent loses children
 * 3. parent gets node
 */
case class InsertNode(node: STripsOntItem, parent: STripsOntItem, children: List[STripsOntItem])

/**
 * 1. parent gets children of node
 * 2. remove node
 */
case class RemoveNode(node: STripsOntItem)

/**
 * 1. Remove the node and all its children and its childrens childrens children.
 */
case class RemoveSubtree(node: STripsOntItem)

/**
 * Move the node and all its children to a new parent
 */
case class GraftNode(node: STripsOntItem, newParent: STripsOntItem)
