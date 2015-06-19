package com.github.mrmechko.strips

/**
 * Created by mechko on 3/26/15.
 */

/**
 * HowTo:
 * Trips type definitions (ie what values a particular feature can take) are instantiated through
 * TripsFeatureDefinition(featureName : String, features : Seq[String]) (or TFD(TripsFeatureType, TripsFeatureValues)
 *
 * After being instantiated, they are always available by instantiating a TripsFeatureType(string) and asking for values.
 *
 *
 * A TripsFeatureTemplateElement is instantiated with a TripsFeatureType and a defaultValue
 *
 * TripsFeatureTemplate contains the string name of the featureset for which this is a template
 * (could be ont:: or just an abstract feature set
 *
 * Templates also contain a parent template, which can be flattened.  A flattened template is memoized so don't flatten
 * until you've loaded all templates (or use flush to clear templates if you need to update them)
 *
 */
/**
 * Type wrapper for feature types
 * @param name
 */
case class TFeatureType(name : String) {
  private def toDefinition = {
    TFeatureDefinition(this)
  }

  def values : Seq[TFeatureVal] = {
    toDefinition.values
  }

  /**
   * Whether a definition for this type has been recorded
   * @return
   */
  def isDefined : Boolean = {
    TFeatureDefinition.index.keySet.contains(this)
  }
}

/**
 * Type wrapper for values
 * @param value
 */
case class TFeatureVal(value : String)








