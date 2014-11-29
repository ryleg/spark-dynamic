package org.apache.spark.repl.adaptor.impl

import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent._

/**
 * Copyright 2014, Radius Intelligence, Inc.
 * All Rights Reserved
 */
trait FutureExt {

  implicit val ec = scala.concurrent.ExecutionContext.Implicits.global

  implicit class getAsFuture(some: Future[Any]) {
    def getAs[T] = Await.result(some, 15.seconds).asInstanceOf[T]
  }

  implicit def getFutureAsString(some: Future[Any]): String = some.getAs[String]

}
