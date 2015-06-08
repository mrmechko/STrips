package com.github.mrmechko.trips

/**
 * Created by mechko on 6/1/15.
 */
object TFeatureTemplate {
  /**
   * Memoize the flattenings so we don't have to reconstruct them every time
   * Indexes *should be* private.
   */
  var flattenedIndex = Map[String, TFeatureTemplate]()
  var index = Map[String, TFeatureTemplate]()

  /**
   * Flush the stored flattened templates.  Only need to use if you update templates already in the index
   */
  def flush = {
    flattenedIndex = Map[String, TFeatureTemplate]()
    basetemplates()
  }
  basetemplates()


  /**
   * Instantiate and add the base templates.  Automagically called.
   */
  def basetemplates() = {
    /*
    (define-feature-list-type F::Phys-obj
    :features (F::object-function F::origin F::form F::mobility F::group F::spatial-abstraction F::intentional F::information F::container F::kr-type f::type F::trajectory)
    :defaults (
	       (F::type ont::phys-object)
	       (F::object-function F::ANY-object-function)
	       (F::origin F::any-origin) (F::Form F::ANY-form)
	       (F::mobility F::ANY-Mobility) (F::group -)
	       (F::Spatial-abstraction (? sab F::spatial-point F::spatial-region))
	       (F::intentional -) (F::trajectory -)
	       (F::information -) (F::container -)
	       ))
     */

    val physobj = TFeatureTemplate("phys-obj", Seq[TFeatureTemplateElement](
      //TripsFeatureTemplateElement("type", "ont::phys-object"),
      TFeatureTemplateElement("object-function", "any-object-function"),
      TFeatureTemplateElement("origin", "any-origin"),
      TFeatureTemplateElement("form", "any-form"),
      TFeatureTemplateElement("mobility", "any-mobility"),
      TFeatureTemplateElement("group", "-") ,
      TFeatureTemplateElement("spatial-abstraction", "spatial-point"), //wut
      TFeatureTemplateElement("intentional", "-"),
      TFeatureTemplateElement("trajectory", "-"),
      TFeatureTemplateElement("information", "-"),
      TFeatureTemplateElement("container", "-")
    ))

    /*
    (define-feature-list-type F::Situation
    :features (F::aspect F::time-span F::cause F::trajectory F::locative F::intentional F::information F::container F::kr-type F::type f::origin f::iobj)
    :defaults (
	       (F::type ont::situation)
	       (F::intentional -)
	       (F::information F::mental-construct) (F::container -)
	       (F::Aspect F::ANY-aspect) (F::Cause F::ANY-Cause)
	       (F::Time-span F::ANY-Time-span) (F::Trajectory -)
	       (F::Locative -)
	       (f::origin f::any-origin)
	       (f::iobj -)
	       ))
     */

    val situation = TFeatureTemplate("situation", Seq[TFeatureTemplateElement](
      //TripsFeatureTemplateElement("type", "ont::situation"),
      TFeatureTemplateElement("intentional", "-"),
      TFeatureTemplateElement("information", "mental-construct"),
      TFeatureTemplateElement("container", "-"),
      TFeatureTemplateElement("aspect", "any-aspect"),
      TFeatureTemplateElement("cause", "any-cause"),
      TFeatureTemplateElement("time-span", "any-time-span"),
      TFeatureTemplateElement("trajectory", "-"),
      TFeatureTemplateElement("locative", "-"),
      TFeatureTemplateElement("origin", "any-origin"),
      TFeatureTemplateElement("iobj", "-")
    ))

    /*
    (define-feature-list-type F::Abstr-obj
      :features (F::measure-function F::scale F::intentional F::information F::container F::gradability F::kr-type f::type f::object-function f::origin f::intensity f::orientation)
    :defaults (
      (F::type ont::abstract-object)
    (F::gradability -)
    (F::orientation -)
    (F::intensity -)
    (F::intentional -)
    (F::information -) (F::container -)
    (F::Measure-function -) (F::scale -)
    (F::object-function F::ANY-object-function)
    (f::origin f::any-origin)
    ))
    */

    val abstrobj = TFeatureTemplate("abstr-obj", Seq[TFeatureTemplateElement](
      //TripsFeatureTemplateElement("type", "ont::abstr-obj"),
      TFeatureTemplateElement("gradability", "-"),
      TFeatureTemplateElement("orientation", "-"),
      TFeatureTemplateElement("intensity", "-"),
      TFeatureTemplateElement("intentional", "-"),
      TFeatureTemplateElement("information", "-"),
      TFeatureTemplateElement("container", "-"),
      TFeatureTemplateElement("measure-function", "-"),
      TFeatureTemplateElement("scale", "-"),
      TFeatureTemplateElement("object-function", "any-object-function"),
      TFeatureTemplateElement("origin", "any-origin")
    ))

    /*
    (define-feature-list-type F::Proposition
    :features (F::intentional F::information F::container F::gradability F::kr-type f::type f::origin)
    :defaults (
	       (F::type ont::any-sem)
	       (F::gradability -)
	       (F::intentional -)
	       (F::information F::information-content) (F::container -)
	       (F::information F::information-content) (F::container -)
	       (f::orig
     */
    val proposition = TFeatureTemplate("proposition", Seq[TFeatureTemplateElement](
      //TripsFeatureTemplateElement("type", "ont::any-sem"),
      TFeatureTemplateElement("gradability", "-"),
      TFeatureTemplateElement("intentional", "-"),
      TFeatureTemplateElement("information", "informatino-content"),
      TFeatureTemplateElement("container",  "-"),
      TFeatureTemplateElement("origin", "any-origin")
    ))


    /*

(define-feature-list-type F::time
    :features (F::time-function f::scale F::time-scale F::kr-type f::type)
    :defaults (
	       (F::type ont::any-time-object)
	       (F::time-function F::Any-time-function)
	       (F::time-scale F::point)
	       (f::scale f::time-measure-scale) ;; a default -- we need f::scale to allow role restriction sharing between durations such as abstract quantities w/ times, like five minutes, which are (f::scale f::duration), and ont::time-intervals
     */
    val time = TFeatureTemplate("time", Seq[TFeatureTemplateElement](
      //TripsFeatureTemplateElement("type", "ont::any-time-object"),
      TFeatureTemplateElement("time-function", "any-time-function"),
      TFeatureTemplateElement("time-scale", "point"),
      TFeatureTemplateElement("scale", "time-measure-scale")
    ))

    this add time
    this add time.copy(name="ont::time")
    this add proposition
    this add proposition.copy(name="ont::proposition")
    this add abstrobj
    this add abstrobj.copy(name="ont::abstr-obj")
    this add situation
    this add situation.copy(name="ont::situation")
    this add physobj
    this add physobj.copy(name="ont::phys-obj")
  }


  /**
   * Add an unflattened template.
   * @param t
   */
  def add(t : TFeatureTemplate) = index = index.updated(t.name, t)

  /**
   * Get a template by string name.  Template names are strings by default because not all templates are attached to a
   * Concept
   * @param t
   * @return
   */
  def get(t : String) : Option[TFeatureTemplate] = index.get(t) match {
    case Some(x) => {
      //System.err.println("found "+t)
      Option(x.flatten)
    }
    case None => {
      //System.err.println("did not find "+t)
      None
    }
  }

  def get(t : TConcept) : Option[TFeatureTemplate] = this get (t name)
}


case class TFeatureTemplateElement(featureType : TFeatureType, default : TFeatureVal)

object TFeatureTemplateElement {
  def apply(ftype : String, default: String) : TFeatureTemplateElement = TFeatureTemplateElement(TFeatureType(ftype), TFeatureVal(default))
}

case class TFeatureTemplate(name : String, features : Seq[TFeatureTemplateElement], inherits : Seq[String] = Seq()) {
  TFeatureTemplate.index = TFeatureTemplate.index
  def flatten : TFeatureTemplate = {
    TFeatureTemplate.flattenedIndex.get(name) match {
      //Take the memoized version first
      case Some(t) => {
        println("FOUND MEMOIZED")
        t
      }
      case None => {
        //load the inherited values first
        var templ = Map[TFeatureType, TFeatureVal]()
        //templ = templ.updated(TripsFeatureType("dummy"), TripsFeatureValue("dumb"))

        //hack to avoid changing dsl with phys-obj etc
        inherits.map(anc=>anc.replace("phys-object", "phys-obj")) foreach { anc =>
          //println(anc)
          //println("get: "+TripsConcept(anc).features)
          // override order is unclear in the case of multiple inheritance
          // Winner is always the thing lower down the hierarchy
          TConcept(anc).features match {
            case Some(a) => {
              //println("got: "+name+":->"+a)
              a.features.map {
                f => {
                  //println(f.featureType.name)
                  templ = templ.updated(f.featureType, f.default)
                }
              }
            }
            case None => //Do Nothing
          }
        }

        //features.foreach(f => println("has feature at %s: %s : %s".format(name, f.featureType.name, f.default.value)))
        features.foreach(f => {
          //println("adding feature at %s: %s : %s".format(name, f.featureType.name, f.default.value))
          templ = templ.updated(f.featureType, f.default)
        })

        //update the index with the flattened template
        val res = TFeatureTemplate(name, templ.map(p => TFeatureTemplateElement(p._1, p._2)).toSeq, inherits)
        //TripsFeatureTemplate.flattenedIndex = TripsFeatureTemplate.flattenedIndex.updated(name, res)
        res
      }
    }
  }
}