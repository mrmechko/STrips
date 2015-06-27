package com.github.mrmechko.strips.model
import monocle.macros.{GenLens, Lenses}

@Lenses("_") case class SFrame(role : String, optional : Boolean, fltype : String, features : List[(SFeatureType, SFeatureVal)])

object SFrame extends IdentifiableCompanion {
  override def prefix: String = ""
}
