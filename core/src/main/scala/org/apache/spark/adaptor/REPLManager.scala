package org.apache.spark.adaptor

import java.io._

import akka.actor.{Props, ActorSystem, Actor}
import org.apache.spark.repl.SparkILoop
import scala.concurrent._
import JSON._

import scala.concurrent.Future
import scala.tools.nsc.interpreter.ILoop
import akka.actor._
import scala.concurrent.Await
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import REPL._

/**
 * Copyright 2014, Radius Intelligence, Inc.
 * All Rights Reserved
 */


class REPLManager(args: Array[String]) extends REPLManagerLike{

  implicit val ec = org.apache.spark.repl.adaptor.REPL.ec

  val in0 = br
  val out = pw

  val interp = new SparkILoop(in0, out)

  val loopFuture = interp.process(args)

  interp.rebindSC(Server.sc)

}
