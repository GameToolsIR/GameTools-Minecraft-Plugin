package ir.taher7.gametools.websocket.events

import ir.taher7.gametools.websocket.Socket
import ir.taher7.gametools.websocket.SocketEvent
import org.sayandev.stickynote.bukkit.log

class ConnectEvent(event: Socket.Event) : SocketEvent(event) {
    override fun handler(event: Array<Any>) {
        log("Successfully Connected to the Websocket.")
    }
}
