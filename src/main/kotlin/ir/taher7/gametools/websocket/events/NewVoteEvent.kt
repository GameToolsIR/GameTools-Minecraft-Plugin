package ir.taher7.gametools.websocket.events

import ir.taher7.gametools.config.messageConfig
import ir.taher7.gametools.core.GameToolsManager
import ir.taher7.gametools.database.Database
import ir.taher7.gametools.database.models.Vote
import ir.taher7.gametools.utils.GsonUtils
import ir.taher7.gametools.utils.GsonUtils.fromJsonOrNull
import ir.taher7.gametools.utils.Utils
import ir.taher7.gametools.websocket.Socket
import ir.taher7.gametools.websocket.SocketEvent
import ir.taher7.gametools.websocket.models.NewVote
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Bukkit
import org.sayandev.stickynote.bukkit.extension.sendComponent
import org.sayandev.stickynote.bukkit.launch
import java.util.*

class NewVoteEvent(event: Socket.Event) : SocketEvent(event) {
    override fun handler(event: Array<Any>) {
        launch {
            val newVote = GsonUtils.gson.fromJsonOrNull(event[0].toString(), NewVote::class.java) ?: return@launch
            val player = newVote.player?.uuid?.let { Bukkit.getPlayer(UUID.fromString(it)) }

            val isVoted = player?.uniqueId?.let { uuid ->
                Database.getUser(uuid).await()?.id?.let { id ->
                    Database.getVote(
                        id,
                        Database.HandleGetType.USER_ID
                    ).await()
                }
            }

            if (isVoted != null) {
                player.sendComponent(messageConfig.vote.alreadyVoted)
                return@launch
            }

            if (newVote.player !== null) {
                val user = Database.getUser(newVote.discordId).await() ?: Database.addUser(
                    newVote.player.uuid,
                    newVote.player.username,
                    newVote.discordId
                ).await()

                Database.addVote(Vote(userId = user.id, isReceivedRewards = player !== null)).await()
                player?.let { GameToolsManager.giveVoteRewards(it) }
            }

            Utils.announce(
                messageConfig.vote.broadcastNewVote,
                Placeholder.parsed("player", player?.name ?: newVote.player?.username ?: newVote.discordDisplayName),
                Placeholder.parsed("server", newVote.serverName),
                Placeholder.parsed("server_rank", newVote.serverVoteRank.toString()),
                Placeholder.parsed("server_votes", newVote.serverVotes.toString()),
                Placeholder.parsed("url", "https://game-tools.ir/mc/servers/${newVote.serverName.lowercase()}"),
            )
        }
    }
}