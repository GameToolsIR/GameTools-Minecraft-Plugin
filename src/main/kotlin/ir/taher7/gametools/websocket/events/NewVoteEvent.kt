package ir.taher7.gametools.websocket.events

import ir.taher7.gametools.api.events.NewVoteEvent
import ir.taher7.gametools.config.messageConfig
import ir.taher7.gametools.config.settingConfig
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
            if (!settingConfig.vote.enable) return@launch
            val newVote = GsonUtils.gson.fromJsonOrNull(event[0].toString(), NewVote::class.java) ?: return@launch
            val player = newVote.player?.uuid?.let { Bukkit.getPlayer(UUID.fromString(it)) }
            val isVoted = player?.uniqueId?.let { uuid ->
                Database.getUser(uuid).await()?.id?.let { id ->
                    Database.getVote(id, Database.HandleGetType.USER_ID).await()
                }
            }

            val newVoteEvent = NewVoteEvent(newVote, isVoted, player)
            Bukkit.getPluginManager().callEvent(newVoteEvent)
            if (newVoteEvent.isCancelled) return@launch

            if (newVoteEvent.isVoted != null) {
                newVoteEvent.player?.sendComponent(messageConfig.vote.alreadyVoted)
                return@launch
            }

            if (newVoteEvent.vote.player !== null) {
                val user = Database.getUser(newVoteEvent.vote.discordId).await() ?: Database.addUser(
                    newVoteEvent.vote.player.uuid,
                    newVoteEvent.vote.player.username,
                    newVoteEvent.vote.discordId
                ).await()

                Database.addVote(Vote(userId = user.id, isReceivedRewards = newVoteEvent.player !== null)).await()
                if (!(settingConfig.vote.getRewardJustInFirstTime && !newVoteEvent.vote.player.isFirstTime)) {
                    newVoteEvent.player?.let { GameToolsManager.giveVoteRewards(it) }
                }
            }

            if (!settingConfig.vote.broadcastMessage) return@launch
            if (settingConfig.vote.onlyBroadcastExistPlayerMessage && newVoteEvent.vote.player === null) return@launch

            Utils.announce(
                messageConfig.vote.broadcastNewVote,
                Placeholder.parsed("player", newVoteEvent.player?.name ?: newVoteEvent.vote.player?.username ?: newVoteEvent.vote.discordDisplayName),
                Placeholder.parsed("server_rank", newVoteEvent.vote.serverVoteRank.toString()),
                Placeholder.parsed("server_votes", newVoteEvent.vote.serverVotes.toString()),
                Placeholder.parsed(
                    "url",
                    "https://game-tools.ir/mc/servers/${GameToolsManager.serverData.name.lowercase()}"
                ),
            )
        }
    }
}