package org.apache.spark.adaptor.impl

import akka.util.{ByteString, Timeout}

import scala.concurrent.Await
import scala.concurrent.duration._

import akka.actor.{ActorSystem, ActorRef}
import akka.pattern.ask
import org.apache.spark.adaptor.JSON
import org.apache.spark.adaptor.REPL._

/**
 * Copyright 2014, Radius Intelligence, Inc.
 * All Rights Reserved
 */
import JSON._


trait AkkaExt {
  implicit val timeout = Timeout(5 seconds)

  implicit class ActorExt(actor: ActorRef) {

    /**
     * Listener actor receives actual data.
     */
      def read() : Unit = {
        actor ! ByteString(JSON.caseClassToJson(REPLMessage(instruction = "read")))
      }

      def write(text: String) = {
        actor ! ByteString(JSON.caseClassToJson(REPLMessage(instruction = "write", text = text)))
      }

  }

  implicit class actorAccessories(as: ActorSystem) {
    def getByPath(actorPath: String) = {
      val checkExistingActor = as.actorSelection(actorPath).resolveOne()
      val foundResult = Await.ready(checkExistingActor, 1.seconds)
      checkExistingActor.value.get
    }
  }


}
