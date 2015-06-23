package com.github.mrmechko.strips.model

case class STripsOntName(id : String, name : String) extends UniquelyIdentifiable

object STripsOntName extends IdentifiableCompanion {
  override def prefix: String = "ONT::"

  def build(name : String) : STripsOntName = {
    if(name.startsWith(prefix)) STripsOntName(name, name)
    else STripsOntName(prefix+name, name)
  }
}
