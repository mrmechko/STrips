package com.github.mrmechko.strips.model

case class SFrame(role : String, optional : Boolean, fltype : String, features : List[(SFeatureType, SFeatureVal)])// TempPlaceHolder

object SFrame extends IdentifiableCompanion {
  override def prefix: String = ""
}

trait IdentifiableCompanion {
  def prefix : String
  def generateId(name : String) : String = prefix+name.stripPrefix(prefix)
}

trait Identifiable {
  def id : String
}

trait UniquelyIdentifiable extends Identifiable {
  override def toString : String = id
}