package com.github.mrmechko.strips.model

trait IdentifiableCompanion {
  def prefix : String
  def generateId(name : String) : String = prefix+name.stripPrefix(prefix)
}

trait Identifiable {
  def id : String
}

trait UniquelyIdentifiable extends Identifiable {
  def u : String = id
}