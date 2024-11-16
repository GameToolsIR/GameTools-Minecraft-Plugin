package ir.taher7.gametools.websocket.events

import ir.taher7.gametools.core.GameToolsManager
import ir.taher7.gametools.utils.GsonUtils
import ir.taher7.gametools.utils.GsonUtils.fromJsonOrNull
import ir.taher7.gametools.utils.Utils
import ir.taher7.gametools.websocket.Socket
import ir.taher7.gametools.websocket.SocketEvent
import ir.taher7.gametools.websocket.models.ServerData
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder

class InitServerDataEvent(event: Socket.Event) : SocketEvent(event) {
    override fun handler(event: Array<Any>) {
        val serverData = GsonUtils.gson.fromJsonOrNull(event[0].toString(), ServerData::class.java) ?: return
        GameToolsManager.serverData = serverData
        Utils.initTagResolvers(
            Placeholder.parsed("server", serverData.name),
            Placeholder.parsed("server_color", "<${serverData.color}>"),
        )
    }
}