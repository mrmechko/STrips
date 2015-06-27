package com.github.mrmechko.strips.json

/**
 * Created by mechko on 6/22/15.
 */

import com.github.mrmechko.strips.model._
import com.github.mrmechko.swordnet.structures.SPos
import play.api.libs.json._
import play.api.libs.functional.syntax._

object Implicits {

  //SFeatureType => get statically
/*  implicit val SFeatureTypeWrites : Writes[SFeatureType] = new Writes[SFeatureType] {
    override def writes(o: SFeatureType): JsValue = Json.obj("name" -> o.name)
  }

  implicit val SFeatureTypeReads : Reads[SFeatureType] = (JsPath \ "name").read[String].map(SFeatureType.is(_))

  //SFeatureVal(id : String, value : String)
  implicit val SFeatureValWrites : Writes[SFeatureVal] = new Writes[SFeatureVal] {
    def writes(fval: SFeatureVal) = Json.obj(
      "value" -> fval.value
    )
  }

  implicit val SFeatureValReads : Reads[SFeatureVal] = (JsPath \ "name").read[String].map(SFeatureVal.apply(_))

*/
  implicit val SFTSVVpairWrites : Writes[(SFeatureType, SFeatureVal)] = new Writes[(SFeatureType, SFeatureVal)] {
    override def writes(o: (SFeatureType, SFeatureVal)): JsValue = Json.obj(
      "feature"->o._1.name,
      "value"->o._2.value
    )
  }

  implicit val SFTSVVpairReads : Reads[(SFeatureType, SFeatureVal)] = (
    (JsPath \ "feature").read[String] and (JsPath \ "value").read[String]
  )(((x,y) => (SFeatureType.is(x),SFeatureVal.build(y))))

  //SFeatureTemplateName(id : String)
  implicit val SFeatureTemplateNameWrites : Writes[SFeatureTemplateName] = new Writes[SFeatureTemplateName] {
    override def writes(o: SFeatureTemplateName): JsValue = Json.obj(
      "id" -> o.id
    )
  }

  implicit val SFeatureTemplateNameReads : Reads[SFeatureTemplateName] = (JsPath \ "id").read[String].map(SFeatureTemplateName.build(_))


  //SFeatureTemplate(id : String, name : SFeatureTemplateName, parents : List[SFeatureTemplateName], instances : Map[SFeatureType, SFeatureVal])
  implicit val SFeatureTemplateWrites : Writes[SFeatureTemplate] = new Writes[SFeatureTemplate] {
    override def writes(o: SFeatureTemplate): JsValue = Json.obj(
      "id" -> o.id,
      "name" -> o.name,
      "parents" -> o.parents,
      "instances" -> o.instances.toList
    )
  }

  implicit val SFeatureTemplateReads : Reads[SFeatureTemplate] = (
      (JsPath \ "id").read[String] and
        (JsPath \ "name").read[SFeatureTemplateName] and
        (JsPath \ "parents").read[List[SFeatureTemplateName]] and
        (JsPath \ "instances").read[List[(SFeatureType, SFeatureVal)]]
    )((id,name,parents,instanceList) => SFeatureTemplate.apply(id,name,parents,instanceList.toMap))

  //STripsOntName(id : String, name : String)
  implicit val STripsOntNameWrites : Writes[STripsOntName] = new Writes[STripsOntName] {
    override def writes(o: STripsOntName): JsValue = Json.obj(
      "name"->o.name
    )
  }

  implicit val STripsOntNameReads : Reads[STripsOntName] = (JsPath \ "name").read[String].map(STripsOntName.build(_))

  //STripsWord(id : String, value : String, pos : SPos, ontTypes : List[STripsOntName])

  implicit val STripsWordWrites : Writes[STripsWord] = new Writes[STripsWord] {
    override def writes(o: STripsWord): JsValue = Json.obj(
      "id" -> o.id,
      "value" -> o.value,
      "pos"-> o.pos.asString,
      "ontTypes" -> o.ontTypes
    )
  }

  implicit val STripsWordReads : Reads[STripsWord] = (
    (JsPath \ "id").read[String] and (JsPath \ "value").read[String] and (JsPath \ "pos").read[String] and (JsPath \ "ontTypes").read[List[STripsOntName]]
    )((x,x1,y,z)=>(STripsWord.apply(x,x1,SPos(y),z)))


  //SFrame(role : String, optional : Boolean, fltype : String, features : List[(SFeatureType, SFeatureVal)])

  implicit val SFrameWrites : Writes[SFrame] = new Writes[SFrame] {
    override def writes(o: SFrame) : JsValue = Json.obj(
      "role" -> o.role,
      "optional" -> o.optional,
      "fltype" -> o.fltype,
      "feats" -> o.features
    )
  }

  implicit val SFrameReads : Reads[SFrame] = (
    (JsPath \ "role").read[String] and
      (JsPath \ "optional").read[Boolean] and
      (JsPath \ "fltype").read[String] and
      (JsPath \ "feats").read[List[(SFeatureType, SFeatureVal)]]
    )(SFrame.apply(_,_,_,_))

  /*STripsOntItem(id : String,
    name : STripsOntName,
    lexicalItems : List[STripsWord],
    wordnetKeys : List[String],
    features : SFeatureTemplate,
    frame : List[SFrame],
    gloss : String,
    examples : List[String]
  )*/

  implicit val STripsOntItemWrites : Writes[STripsOntItem] = new Writes[STripsOntItem] {
    override def writes(o: STripsOntItem): JsValue = Json.obj(
      "id" -> o.id,
      "ont" -> o.name,
      "lex" -> o.lexicalItems,
      "wordNetKeys" -> o.wordnetKeys,
      "feats" -> o.features,
      "frames" -> o.frame,
      "gloss" -> o.gloss,
      "examples" -> o.examples
    )
  }

  implicit val STripsOntItemReads : Reads[STripsOntItem] = (
    (JsPath \ "id").read[String] and
      (JsPath \ "ont").read[STripsOntName] and
      (JsPath \ "lex").read[List[STripsWord]] and
      (JsPath \ "wordNetKeys").read[List[String]] and
      (JsPath \ "feats").read[SFeatureTemplate] and
      (JsPath \ "frames").read[List[SFrame]] and
      (JsPath \ "gloss").read[String] and
      (JsPath \ "examples").read[List[String]]
    )(STripsOntItem.apply(_,_,_,_,_,_,_,_))

  implicit val STOSTOTupleWrites : Writes[(STripsOntName, STripsOntName)] = new Writes[(STripsOntName, STripsOntName)] {
    override def writes(o: (STripsOntName, STripsOntName)): JsValue = Json.obj(
      "child"->o._1,
      "parent"->o._2
    )
  }

  implicit val STOSTOTupleReads : Reads[(STripsOntName, STripsOntName)] =
    ((JsPath \ "child").read[STripsOntName] and (JsPath \ "parent").read[STripsOntName])((_,_))

  //STripsOntology(version : String, nodes : List[STripsOntItem], words : List[STripsWord], inheritance : Map[STripsOntName, STripsOntName]) {
  implicit val STripsOntologyWrites : Writes[STripsOntology] = new Writes[STripsOntology] {
    override def writes(o: STripsOntology): JsValue = Json.obj(
      "version" -> o.version,
      "nodes" -> o.nodes,
      "words" -> o.words,
      "inheritance" -> o.inheritance.toList
    )
  }

  implicit val STripsOntologyReads : Reads[STripsOntology] = (
    (JsPath \ "version").read[String] and
      (JsPath \ "nodes").read[List[STripsOntItem]] and
      (JsPath \ "words").read[List[STripsWord]] and
      (JsPath \ "inheritance").read[List[(STripsOntName, STripsOntName)]]
    )((v,n,w,i) => (STripsOntology(v, n, w, i.toMap)))
}
