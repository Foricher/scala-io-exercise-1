package com.xebia.exercise1

import spray.testkit.Specs2RouteTest
import org.specs2.mutable.Specification
import spray.http.StatusCodes

import spray.httpx.SprayJsonSupport._
import akka.actor.{Props, ActorRef, ActorRefFactory}

class ReceptionistSpec extends Specification with Specs2RouteTest {

  val subject = new ReverseRoute {
    implicit def actorRefFactory: ActorRefFactory = system
    implicit def executionContext = system.dispatcher

    def createChild(props:Props, name:String): ActorRef = system.actorOf(props, name)
  }

  "The Receptionist" should {
    "Respond with a JSON response that contains a reversed string value" in {

      Post("/reverse", ReverseRequest("some text to reverse")) ~> subject.reverseRoute ~> check {
        status === StatusCodes.OK
        val response = entityAs[ReverseResponse]
        response.value must beEqualTo("esrever ot txet emos")
      }

    }
  }
}
