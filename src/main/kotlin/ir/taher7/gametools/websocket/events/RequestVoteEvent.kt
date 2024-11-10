package ir.taher7.gametools.websocket.events

import ir.taher7.gametools.utils.GsonUtils
import ir.taher7.gametools.utils.GsonUtils.fromJsonOrNull
import ir.taher7.gametools.websocket.Socket
import ir.taher7.gametools.websocket.SocketEvent
import ir.taher7.gametools.websocket.models.RequestVote
import org.bukkit.Bukkit
import org.sayandev.stickynote.bukkit.extension.sendComponent
import java.util.*

class RequestVoteEvent(event: Socket.Event) : SocketEvent(event) {
    override fun handler(event: Array<Any>) {
        val requestVote = GsonUtils.gson.fromJsonOrNull(event[0].toString(), RequestVote::class.java) ?: return
        val player = Bukkit.getPlayer(UUID.fromString(requestVote.uuid)) ?: return
        player.sendComponent("<click:open_url:'${requestVote.url}'>Click to Vote</click>")
    }
}