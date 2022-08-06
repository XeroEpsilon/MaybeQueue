package Queue

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.{Attributes, FlowShape, Inlet, Outlet}
import akka.stream.scaladsl.Source
import akka.stream.stage.{GraphStage, GraphStageLogic, InHandler, OutHandler}

import scala.collection.mutable.ListBuffer
import scala.concurrent.Future

class Queue(size: Int) extends GraphStage[FlowShape[Message, Message]]{

  val in: Inlet[Message] = Inlet[Message]("Queue.in")
  val out: Outlet[Message] = Outlet[Message]("Queue.out")

  override def shape: FlowShape[Message, Message] = FlowShape.of(in, out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic = {
    new GraphStageLogic(shape) {

      val queue = new ListBuffer[Message]

      setHandler(in, new InHandler {
        override def onPush(): Unit = {
          if (queue.length > size) {
            println("Ignore")
            return
          } else {
            queue.addOne(grab(in))
          }
          if (queue.nonEmpty) {
            push(out, queue.last)
          }
        }
      })

      setHandler(out, new OutHandler {
        override def onPull(): Unit = {
          pull(in)
        }
      })

    }
  }

}
