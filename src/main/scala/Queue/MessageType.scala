package Queue

object MessageType extends Enumeration {
  type MessageType = Value

  val T1: MessageType.Value = Value("t1")
  val T2: MessageType.Value = Value("t2")
  val T3: MessageType.Value = Value("t3")
  val T4: MessageType.Value = Value("t4")
  val ALL: MessageType.Value = Value("all")
}