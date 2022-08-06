import Queue.{SinkConsumer, Message, MessageType, Publisher, Queue}
import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.{ClosedShape, FlowShape, Graph, SinkShape, SourceShape}
import akka.stream.scaladsl.{Broadcast, Flow, GraphDSL, RunnableGraph, Sink, Source}

import scala.collection.mutable.ListBuffer
import scala.util.Try

object Main extends App {
  println("Test")
  implicit val system: ActorSystem = ActorSystem("QuickStart")

  val sourceGraph: Graph[SourceShape[Message], NotUsed] = new Publisher
  val mySource: Source[Message, NotUsed] = Source.fromGraph(sourceGraph)

  val sinkGraph: Graph[SinkShape[Message], NotUsed] = new SinkConsumer((m: Message) => {
    Try {
      println("1 - " + m.message)
      m.message.toInt
    }
  })
  val mySink = Sink.fromGraph(sinkGraph)

  val sinkGraph1: Graph[SinkShape[Message], NotUsed] = new SinkConsumer((m: Message) => {
    Try {
      println("2 - " + m.message)
      Thread.sleep(1000)
      m.message.toInt
    }
  })
  val mySink1: Sink[Message, NotUsed] = Sink.fromGraph(sinkGraph1)

  val qGraph: Graph[FlowShape[Message, Message], NotUsed] = new Queue(10)
  val queue: Flow[Message, Message, NotUsed] = Flow.fromGraph(qGraph)

  val consumers = new ListBuffer[Sink[Message, NotUsed]]()
  consumers.addOne(mySink)
  consumers.addOne(mySink1)

  mySource.take(100).via(queue).filter((m: Message) => m.messageType.equals(MessageType.T3)).runWith(mySink)
  mySource.take(100).via(queue).runWith(mySink1)
  mySource.take(100).via(queue).map((m: Message) => {
    Try {
      println("3 - " + m.message)
      m.message.toInt
    }
  }).map((m) => m.getOrElse(10000))
    .runWith(Sink.foreach((m: Int) => println("3.1 - " + (m + 1000))))

}
