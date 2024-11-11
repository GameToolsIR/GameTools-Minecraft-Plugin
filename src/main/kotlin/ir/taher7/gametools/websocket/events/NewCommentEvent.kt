package ir.taher7.gametools.websocket.events

import ir.taher7.gametools.utils.GsonUtils
import ir.taher7.gametools.utils.GsonUtils.fromJsonOrNull
import ir.taher7.gametools.websocket.Socket
import ir.taher7.gametools.websocket.SocketEvent
import ir.taher7.gametools.websocket.models.NewComment
import org.bukkit.Bukkit
import org.sayandev.stickynote.bukkit.extension.sendComponent
import org.sayandev.stickynote.bukkit.log

class NewCommentEvent(socket: Socket.Event) : SocketEvent(socket) {
    override fun handler(event: Array<Any>) {
        val newComment = GsonUtils.gson.fromJsonOrNull(event[0].toString(), NewComment::class.java) ?: return

        log(newComment.discordId)
        log(newComment.discordDisplayName)
        log(newComment.content)
        log(newComment.serverName)
        log(newComment.rating.toString())
        log(newComment.serverRating.toString())

        for (player in Bukkit.getOnlinePlayers()) {
            player.sendComponent("<yellow> New comment from <green>${newComment.discordDisplayName}<yellow>.")
            player.sendComponent("<yellow>${newComment.content}")
        }

    }
}