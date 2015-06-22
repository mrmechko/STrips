package com.github.mrmechko.strips.model

import com.github.mrmechko.swordnet.structures.SPos

/**
 * Created by mechko on 6/22/15.
 */

case class STripsWord(id : String, value : String, pos : SPos, ontTypes : List[STripsOntName]) extends Identifiable

object STripsWord extends IdentifiableCompanion{
  override def prefix: String = "W::"

  def apply(value : String, pos : SPos, ontTypes : List[STripsOntName]) : STripsWord = {
    STripsWord(prefix+value.stripPrefix(prefix)+"_%s".format(pos.asString), value, pos, ontTypes)
  }

}
