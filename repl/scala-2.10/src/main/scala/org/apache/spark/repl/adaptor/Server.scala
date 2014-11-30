
package org.apache.spark.repl.adaptor

import java.io.OutputStream
import java.net.InetSocketAddress

import akka.actor.{ActorRef, ActorSystem, Props, Actor}
import akka.io.Tcp.{Write, Register, Received}
import akka.io.{Tcp, IO}
import akka.util.{ByteString, ByteIterator, Timeout}
import org.apache.spark.{SparkEnv, SparkConf, SparkContext}
import org.json4s._
import REPL._
import akka.actor._
import scala.concurrent.duration._

object Server{

  def createSparkContext(): SparkContext = {
    val execUri = System.getenv("SPARK_EXECUTOR_URI")
    val conf = new SparkConf()
      .setMaster(getMaster())
      .setAppName("Spark shell")
    if (execUri != null) {
      conf.set("spark.executor.uri", execUri)
    }
    sc = new SparkContext(conf)
    sc


  }

  def getMaster(): String = {
    val envMaster = sys.env.get("MASTER")
    val propMaster = sys.props.get("spark.master")
    val master =  propMaster.orElse(envMaster).getOrElse("local[*]")
    master
  }

  var sc : SparkContext = _

  val defaultPort = 16180

  def main(args: Array[String]) {
    createSparkContext()
    implicit val as = SparkEnv.get.actorSystem

    start(args)
  }

  def start(args: Array[String], portOffset: Int = 0)(implicit as: ActorSystem) : ActorRef = {
    as.actorOf(Props(new Server(args, portOffset)))
  }

}


class Server(args: Array[String], portOffset: Int) extends Actor {
  implicit val timeout = Timeout(15 seconds)

  import Server._
  import akka.io.Tcp._
  import context.system

  IO(Tcp) ! Bind(self, new InetSocketAddress("0.0.0.0", defaultPort+portOffset))

  val replM = context.actorOf(Props(new REPLManager(args)))

  def receive = {
    case b @ Bound(localAddress) =>
      // do some logging or setup ...
      println(s"bound @ $b")

    case CommandFailed(_: Bind) => context stop self

    case c @ Connected(remote, local) =>
      println(s"Connected remote $remote local $local")
      val handler = context.actorOf(Props(new REPLHandler(replM)))
      val connection = sender
      connection ! Register(handler)
  }

}
