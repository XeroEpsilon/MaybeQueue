package Queue

import akka.NotUsed
import akka.stream.{Attributes, Outlet, SourceShape}
import akka.stream.stage.{GraphStage, GraphStageLogic, OutHandler}

class Publisher extends GraphStage[SourceShape[Message]]{

  val out: Outlet[Message] = Outlet("NumbersSource")

  override def shape: SourceShape[Message] = SourceShape(out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic = {
    new GraphStageLogic(shape) {
      private var counter = 1

      setHandler(out, new OutHandler {
        override def onPull(): Unit = {
          push(out, new Message(counter.toString, MessageType.T3))
          counter += 1
        }
      })
    }
  }
}
