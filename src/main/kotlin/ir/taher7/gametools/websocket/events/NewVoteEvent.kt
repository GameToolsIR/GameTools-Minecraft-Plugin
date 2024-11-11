package ir.taher7.gametools.websocket.events

import ir.taher7.gametools.core.GameToolsManager
import ir.taher7.gametools.database.Database
import ir.taher7.gametools.database.models.Vote
import ir.taher7.gametools.utils.GsonUtils
import ir.taher7.gametools.utils.GsonUtils.fromJsonOrNull
import ir.taher7.gametools.websocket.Socket
import ir.taher7.gametools.websocket.SocketEvent
import ir.taher7.gametools.websocket.models.NewVote
import org.bukkit.Bukkit
import org.sayandev.stickynote.bukkit.extension.sendComponent
import org.sayandev.stickynote.bukkit.launch
import java.util.*

class NewVoteEvent(event: Socket.Event) : SocketEvent(event) {
    override fun handler(event: Array<Any>) {
        launch {
            val newVote = GsonUtils.gson.fromJsonOrNull(event[0].toString(), NewVote::class.java) ?: return@launch
            val player = newVote.player?.uuid?.let { Bukkit.getPlayer(UUID.fromString(it)) }

            val isVoted = Database.getVote(newVote.discordId).await()

            if (isVoted != null) {
                player?.sendComponent("You have already voted! and received rewards.")
                return@launch
            }

            if (newVote.player !== null) {
                val newVote = Vote(
                    uuid = newVote.player.uuid,
                    username = newVote.player.username,
                    discordId = newVote.discordId,
                    isReceivedRewards = player === null,
                )

                Database.addVote(newVote).await()
                player?.let { GameToolsManager.giveVoteRewards(it) }
            }


            for (player in Bukkit.getOnlinePlayers()) {
                player.sendComponent("<yellow>A new vote has been received from <green>${newVote.discordDisplayName} ${if (newVote.player !== null) "<blue>${newVote.player.username}" else ""}<yellow>.")
            }
        }

    }
}