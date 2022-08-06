package Queue

import Queue.MessageType.MessageType

class Message(m: String, mt: MessageType) {
  val message: String = m
  val messageType: MessageType = mt
}
