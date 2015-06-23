package com.github.mrmechko.strips.model

import com.github.mrmechko.swordnet.structures.SKey

/**
 * Created by mechko on 6/22/15.
 */

case class STripsOntItem(id : String,
                         name : STripsOntName,
                         lexicalItems : List[STripsWord],
                         wordnetKeys : List[String],
                         features : SFeatureTemplate,
                         frame : List[SFrame]
                        ) extends UniquelyIdentifiable

object STripsOntItem extends IdentifiableCompanion{
  override def prefix: String = "ONT::"

  def build(
             name : STripsOntName,
             lexicalItems : List[STripsWord],
             wordnetKeys : List[String],
             features : SFeatureTemplate,
             frame : List[SFrame]
           ) : STripsOntItem = STripsOntItem(generateId(name.id), name, lexicalItems, wordnetKeys, features, frame)
}
