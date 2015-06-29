package com.github.mrmechko.strips.modify
import com.github.mrmechko.strips.model._
import monocle.macros.{ GenLens, Lenses }
import monocle.Lens

trait ModifyOnt {
  def apply(ont: STripsOntology): Option[STripsOntology]
  def isValidOn(ont: STripsOntology): Boolean
  def canCommuteWith(other: ModifyOnt): Boolean
  def isIndependentOf(other: ModifyOnt): Boolean
  def affects(ont: STripsOntology): List[STripsOntName]
}

/* trait ContainerOf[X] {
  def isContainerOf(a: Any): Boolean = {
    case that: X => true
    case _ => false
  }
} */

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
trait ModOntItemElement[T] extends ModifyOnt {
  def target: STripsOntName
  def element: T
  def generate(ont: STripsOntology): Option[STripsOntItem]
}

trait ChainableModOntItemElement extends ModifyOnt {
  def genReplacement(ont : STripsOntology) : Option[(Int, STripsOntItem)]
}

/**
 * Provide an STripsOntItemName and the replacement element
 **/
case class ReplaceGloss(target: STripsOntName, gloss: String) extends ModOntItemElement[String] with ChainableModOntItemElement {
  override def element: String = gloss
  override def generate(ont: STripsOntology): Option[STripsOntItem] = {
    if (isValidOn(ont)) Some(ModifyElement.gloss.set(gloss)(ont.nodeByName(target)))
    else None
  }
  override def affects(ont : STripsOntology): List[STripsOntName] = List(target)
  override def canCommuteWith(other: ModifyOnt): Boolean = other match {
    case that: this.type => that.target != this.target
    case _ => true
  }
  override def isIndependentOf(other: ModifyOnt): Boolean = !canCommuteWith(other)
  override def isValidOn(ont: STripsOntology): Boolean = ont.nameToIndex.get(target) match {
    case Some(_) => true
    case _ => false
  }
  override def genReplacement(ont : STripsOntology) : Option[(Int, STripsOntItem)] = generate(ont).map(rep => {
    ont.nameToIndex(target) -> rep
  })

  def apply(ont: STripsOntology): Option[STripsOntology] = {
    if (!isValidOn(ont)) None
    else {
      this.generate(ont).map(o => {
        def newNodes = {
          ont.nodes.updated(ont.nameToIndex(target), o)
        }
        STripsOntology._nodes.set(newNodes)(ont)
      })
    }
  }
}

case class ReplaceMultipleGlosses(repOps : List[ReplaceGloss]) extends ModifyOnt {
  override def apply(ont: STripsOntology): Option[STripsOntology] = {
    if (!isValidOn(ont)) None
    else {
      val nodes = ont.nodes.toArray
      //Update the nodes quickly in an array
      repOps.map(_.genReplacement(ont)).foreach(b => b match {
        case Some(rep) => nodes(rep._1) = rep._2
        case _ =>
      })
      Some(STripsOntology._nodes.set(nodes.toList)(ont))
    }
  }
  override def isValidOn(ont: STripsOntology): Boolean = repOps.foldLeft(true)((a, b) => a && b.isValidOn(ont))
  override def canCommuteWith(other: ModifyOnt): Boolean = repOps.foldLeft(true)((a, b) => a && b.canCommuteWith(other))
  override def isIndependentOf(other: ModifyOnt): Boolean =repOps.foldLeft(true)((a, b) => a && b.isIndependentOf(other))
  override def affects(ont: STripsOntology): List[STripsOntName] = repOps.flatMap(_.affects(ont)).distinct
  def add(next : ReplaceGloss) : Option[ReplaceMultipleGlosses] = if(isIndependentOf(next)) {
    Some(this.copy(repOps = this.repOps.+:(next)))
  } else {None}
}

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
