package ir.taher7.gametools.websocket.events

import ir.taher7.gametools.utils.GsonUtils
import ir.taher7.gametools.utils.GsonUtils.fromJsonOrNull
import ir.taher7.gametools.websocket.Socket
import ir.taher7.gametools.websocket.SocketEvent
import ir.taher7.gametools.websocket.models.Error
import org.sayandev.stickynote.bukkit.log

class ErrorEvent(event: Socket.Event) : SocketEvent(event) {
    override fun handler(event: Array<Any>) {
        val error = GsonUtils.gson.fromJsonOrNull(event[0].toString(), Error::class.java)
        if (error !== null) {
            log("Error: ${error.message}")
        } else {
            log("Error: ${event[0]}")
        }
    }
}