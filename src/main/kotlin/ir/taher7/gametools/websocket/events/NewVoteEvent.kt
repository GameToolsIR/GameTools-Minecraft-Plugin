package ir.taher7.gametools.websocket.events

import com.google.gson.internal.LinkedTreeMap
import ir.taher7.gametools.utils.GsonUtils
import ir.taher7.gametools.utils.GsonUtils.fromJsonOrNull
import ir.taher7.gametools.websocket.Socket
import ir.taher7.gametools.websocket.SocketEvent
import ir.taher7.gametools.websocket.models.NewVote
import org.sayandev.stickynote.bukkit.log

class NewVoteEvent(event: Socket.Event) : SocketEvent(event) {
    override fun handler(event: Array<Any>) {
        val newVote = GsonUtils.gson.fromJsonOrNull(event[0].toString(), NewVote::class.java) ?: return
        log("New Vote: ${newVote.votes}")
        log("New Vote: ${newVote.uuid}")
    }
}