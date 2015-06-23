package com.github.mrmechko.strips.model

case class SFrame(role : String, optional : Boolean, fltype : String, features : List[(SFeatureType, SFeatureVal)])

object SFrame extends IdentifiableCompanion {
  override def prefix: String = ""
}