package ir.taher7.gametools.websocket

import com.google.gson.GsonBuilder
import com.google.gson.internal.LinkedTreeMap
import io.socket.client.IO
import io.socket.client.Socket
import ir.taher7.gametools.config.settingConfig
import ir.taher7.gametools.utils.GsonUtils
import ir.taher7.gametools.utils.GsonUtils.fromJsonOrNull
import ir.taher7.gametools.websocket.events.ConnectEvent
import ir.taher7.gametools.websocket.events.DisconnectEvent
import ir.taher7.gametools.websocket.events.ErrorEvent
import ir.taher7.gametools.websocket.events.VoteEvent
import ir.taher7.gametools.websocket.models.Error
import org.sayandev.stickynote.bukkit.log
import java.net.URI

object Socket {

    private val socket: Socket

    enum class Event(val value: String) {
        CONNECT(Socket.EVENT_CONNECT),
        ERROR(Socket.EVENT_CONNECT_ERROR),
        DISCONNECT(Socket.EVENT_DISCONNECT),
        VOTE("vote"),

    }

    private val url =
        URI.create("ws${if (settingConfig.websocket.useSSL) "s" else ""}://${settingConfig.websocket.host}:${settingConfig.websocket.port}/ws/server")
    private val auth = mapOf(
        "token" to settingConfig.websocket.token,
    )
    private val options = IO.Options.builder()
        .setAuth(auth)
        .setTransports(arrayOf("websocket"))
        .build()

    init {
        socket = IO.socket(url, options)
        socket.connect()


        socket.on(Event.CONNECT.value) { ConnectEvent(Event.CONNECT).handler(it) }
        socket.on(Event.ERROR.value) { ErrorEvent(Event.ERROR).handler(it) }
        socket.on(Event.DISCONNECT.value) { DisconnectEvent(Event.DISCONNECT).handler(it) }
        socket.on(Event.VOTE.value) { VoteEvent(Event.VOTE).handler(it) }

    }


    fun emit(event: String, vararg args: Any) {
        if (!socket.isActive) return
        socket.emit(event, args)
    }

    fun close() {
        socket.close()
    }

    fun isActive(): Boolean {
        return socket.isActive
    }
}