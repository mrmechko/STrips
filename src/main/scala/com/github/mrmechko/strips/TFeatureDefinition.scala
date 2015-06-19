package com.github.mrmechko.strips

/**
 * Object for TripsFeatureDefinitions.
 * New definitions are created by applying with a feature name and a list of values
 */
object TFeatureDefinition {
  var index = Map[TFeatureType, Seq[TFeatureVal]]()

  def apply(name : String, values : Seq[String]) : TFeatureDefinition = {
    val definition = values.map(TFeatureVal)
    val tname = TFeatureType(name)
    apply(tname, definition)
  }

  def apply(name : TFeatureType, values : Seq[TFeatureVal]) : TFeatureDefinition = {
    index = index.updated(name, values)
    TFeatureDefinition(name)
  }
}

/**
 * Lookup object for Feature Definitions
 * These can be ignored for now.
 * @param name
 */
case class TFeatureDefinition(name : TFeatureType) {
  def values : Seq[TFeatureVal] = {
    TFeatureDefinition.index.getOrElse(name, Seq[TFeatureVal]())
  }
}
