package com.github.mrmechko.strips.model
import monocle.macros.{GenLens, Lenses}


sealed trait SFeatureTemplateName extends UniquelyIdentifiable

object SFeatureTemplateName extends IdentifiableCompanion {
  override def prefix = "T::"

  private case class SFeatureTemplateNameImpl(id : String) extends SFeatureTemplateName

  def apply(name : String) : SFeatureTemplateName = {
    SFeatureTemplateNameImpl(generateId(name))
  }

  def build(name : String) : SFeatureTemplateName = apply(name)
}

@Lenses("_") case class SFeatureTemplate(id : String, name : SFeatureTemplateName, parents : List[SFeatureTemplateName], instances : Map[SFeatureType, SFeatureVal]) extends UniquelyIdentifiable

object SFeatureTemplate extends IdentifiableCompanion {
  override def prefix: String = "T::"
  //There are only 4 predef templates
  def build(name : String, parents : String, instances : Map[SFeatureType, SFeatureVal]) : SFeatureTemplate = {
    val n = SFeatureTemplateName(name)
    val np = if (parents.startsWith("(?")) {
      List[SFeatureTemplateName]() //TODO
    } else {
      List[SFeatureTemplateName](SFeatureTemplateName(parents))
    }
    SFeatureTemplate(generateId(n.id), n, np, instances)
  }
}


sealed trait SFeatureType extends UniquelyIdentifiable {
  def name : String
}

object SFeatureType extends IdentifiableCompanion {
  override def prefix: String = "F::"
  private case class SFeatureTypeImpl$(name : String) extends SFeatureType {
    override def id: String = prefix+name.stripPrefix(SFeatureType.prefix)

    override def toString : String = "SFeatureType(%s)".format(name)
  }

  val aspect : SFeatureType = SFeatureTypeImpl$("aspect")
  val cause : SFeatureType = SFeatureTypeImpl$("cause")
  val container : SFeatureType = SFeatureTypeImpl$("container")
  val form : SFeatureType = SFeatureTypeImpl$("form")
  val function : SFeatureType = SFeatureTypeImpl$("function")
  val gradability : SFeatureType = SFeatureTypeImpl$("gradability")
  val group : SFeatureType = SFeatureTypeImpl$("group")
  val information : SFeatureType = SFeatureTypeImpl$("information")
  val intentional : SFeatureType = SFeatureTypeImpl$("intentional")
  val locative : SFeatureType = SFeatureTypeImpl$("locative")
  val mobility : SFeatureType = SFeatureTypeImpl$("mobility")
  val origin : SFeatureType = SFeatureTypeImpl$("mobility")
  val scale : SFeatureType = SFeatureTypeImpl$("scale")
  val span : SFeatureType = SFeatureTypeImpl$("span")
  val trajectory : SFeatureType = SFeatureTypeImpl$("trajectory")

  val features = List(aspect, cause, container, form, function, gradability, group, information, intentional, locative, mobility, origin, scale, span, trajectory)

  private val map : Map[String, SFeatureType] = features.map(x => x.name -> x).toMap
  def is(name : String) : SFeatureType = {
    map(name)
  }

  def get(name : String) : Option[SFeatureType] = {
    map.get(name)
  }
}

case class SFeatureVal(id : String, value : String) extends UniquelyIdentifiable

object SFeatureVal extends IdentifiableCompanion {
  override def prefix: String = "VAL::"
  def apply(value : String) : SFeatureVal = {
    SFeatureVal(prefix + value, value)
  }

  def build(value : String) : SFeatureVal = apply(value)
  /*def unapply(f : SFeatureVal) : Option[(String, String)] = {
    Some((f.id, f.value))
  }*/
}
