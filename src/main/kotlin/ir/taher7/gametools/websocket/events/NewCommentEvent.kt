package ir.taher7.gametools.websocket.events

import ir.taher7.gametools.utils.GsonUtils
import ir.taher7.gametools.utils.GsonUtils.fromJsonOrNull
import ir.taher7.gametools.utils.Utils
import ir.taher7.gametools.websocket.Socket
import ir.taher7.gametools.websocket.SocketEvent
import ir.taher7.gametools.websocket.models.NewComment
import org.bukkit.Bukkit
import org.sayandev.stickynote.bukkit.extension.sendComponent
import org.sayandev.stickynote.bukkit.log

class NewCommentEvent(socket: Socket.Event) : SocketEvent(socket) {
    override fun handler(event: Array<Any>) {
//        val newComment = GsonUtils.gson.fromJsonOrNull(event[0].toString(), NewComment::class.java) ?: return
//
//
//        Utils.announce("<yellow>A new comment has been received from <green>${newComment.discordDisplayName}<yellow>.")
//        Utils.announce("<yellow>${newComment.content}")

    }
}