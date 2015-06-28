package com.github.mrmechko.strips.model

import monocle.macros.{GenLens, Lenses}

import com.github.mrmechko.swordnet.structures.SKey

/**
 * Created by mechko on 6/22/15.
 */

@Lenses("_") case class STripsOntItem(id : String,
                         name : STripsOntName,
                         lexicalItems : List[STripsWord],
                         wordnetKeys : List[String],
                         features : SFeatureTemplate,
                         frame : List[SFrame],
                         gloss : String = "",
                         examples : List[String] = List()
                        ) extends UniquelyIdentifiable

object STripsOntItem extends IdentifiableCompanion{
  override def prefix: String = "ONT::"

  def build(
             name : STripsOntName,
             lexicalItems : List[STripsWord],
             wordnetKeys : List[String],
             features : SFeatureTemplate,
             frame : List[SFrame],
             gloss : String = "",
             examples : List[String] = List()
           ) : STripsOntItem = STripsOntItem(generateId(name.id), name, lexicalItems, wordnetKeys, features, frame, gloss, examples)
}
