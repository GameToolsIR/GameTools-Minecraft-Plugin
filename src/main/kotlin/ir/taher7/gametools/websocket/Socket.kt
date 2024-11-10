package ir.taher7.gametools.websocket

import io.socket.client.IO
import io.socket.client.Socket
import ir.taher7.gametools.config.settingConfig
import ir.taher7.gametools.websocket.events.*
import java.net.URI

object Socket {

    private val socket: Socket

    enum class Event(val value: String) {
        CONNECT(Socket.EVENT_CONNECT),
        ERROR(Socket.EVENT_CONNECT_ERROR),
        DISCONNECT(Socket.EVENT_DISCONNECT),
        REQUEST_VOTE("requestVote"),
        NEW_VOTE("newVote"),
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
        socket.on(Event.REQUEST_VOTE.value) { RequestVoteEvent(Event.REQUEST_VOTE).handler(it) }
        socket.on(Event.NEW_VOTE.value) { NewVoteEvent(Event.NEW_VOTE).handler(it) }

    }


    fun emit(event: String, data: Map<String, String>) {
        if (!socket.isActive) return
        socket.emit(event, data)
    }

    fun close() {
        socket.close()
    }

    fun isActive(): Boolean {
        return socket.isActive
    }
}