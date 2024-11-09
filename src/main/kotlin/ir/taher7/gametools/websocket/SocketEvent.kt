package ir.taher7.gametools.websocket

abstract class SocketEvent(
    private val event: Socket.Event
) {
    abstract fun handler(event: Array<Any>): Unit
}