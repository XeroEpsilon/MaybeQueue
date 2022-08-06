package Queue

import akka.Done
import akka.stream.{Attributes, Inlet, Outlet, SinkShape}
import akka.stream.stage.{GraphStage, GraphStageLogic, InHandler, OutHandler}

import scala.util.Try

class SinkConsumer(f: Message => Try[Int]) extends GraphStage[SinkShape[Message]]{

  val in: Inlet[Message] = Inlet("ConsumerSink")

  override def shape: SinkShape[Message] = SinkShape(in)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic = {
    new GraphStageLogic(shape) {
      override def preStart(): Unit = pull(in)

      setHandler(in, new InHandler {
        override def onPush(): Unit = {
          val message = grab(in)
          var result = Try(1)
          var i = 0
          do{
            result = f(message)
            i = i + 1
          } while (result.isFailure && i < 10)
          pull(in)
        }
      })
    }
  }


}
