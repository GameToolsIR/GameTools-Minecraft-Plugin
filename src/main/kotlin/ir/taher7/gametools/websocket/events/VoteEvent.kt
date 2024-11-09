package ir.taher7.gametools.websocket.events

import com.google.gson.internal.LinkedTreeMap
import ir.taher7.gametools.websocket.Socket
import ir.taher7.gametools.websocket.SocketEvent

class VoteEvent(event: Socket.Event) : SocketEvent(event) {
    override fun handler(event: Array<Any>) {
        val data = event[0] as LinkedTreeMap<*, *>
        val vote = data["vote"] as String
        val count = data["count"] as Double
    }
}