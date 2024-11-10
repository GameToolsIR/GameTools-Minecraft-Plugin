package ir.taher7.gametools.websocket.events

import ir.taher7.gametools.utils.GsonUtils
import ir.taher7.gametools.utils.GsonUtils.fromJsonOrNull
import ir.taher7.gametools.websocket.Socket
import ir.taher7.gametools.websocket.SocketEvent
import ir.taher7.gametools.websocket.models.NewVote
import org.bukkit.Bukkit
import org.sayandev.stickynote.bukkit.extension.sendComponent
import org.sayandev.stickynote.bukkit.log
import java.util.*

class NewVoteEvent(event: Socket.Event) : SocketEvent(event) {
    override fun handler(event: Array<Any>) {
        val newVote = GsonUtils.gson.fromJsonOrNull(event[0].toString(), NewVote::class.java) ?: return
        log(event[0].toString())
        log(newVote.toString())

        val player = newVote.player?.uuid?.let { Bukkit.getPlayer(UUID.fromString(it)) }

        if (player !== null) player.sendComponent("You have voted for the server ${newVote.serverVotes} times.")

        newVote.player?.uuid?.let { log(it) } ?: log("null")
        newVote.player?.username?.let { log(it) } ?: log("null")
        newVote.player?.isFirstTime?.let { log(it.toString()) } ?: log("null")

        log(newVote.discordId)
        log("New vote from ${newVote.discordDisplayName} with ${newVote.serverVotes} votes.")

    }
}