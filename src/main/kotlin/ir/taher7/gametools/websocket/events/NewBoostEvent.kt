package ir.taher7.gametools.websocket.events

import ir.taher7.gametools.config.messageConfig
import ir.taher7.gametools.core.GameToolsManager
import ir.taher7.gametools.database.Database
import ir.taher7.gametools.utils.GsonUtils
import ir.taher7.gametools.utils.GsonUtils.fromJsonOrNull
import ir.taher7.gametools.utils.Utils
import ir.taher7.gametools.websocket.Socket
import ir.taher7.gametools.websocket.SocketEvent
import ir.taher7.gametools.websocket.models.NewBoost
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Bukkit
import org.sayandev.stickynote.bukkit.launch
import java.util.*

class NewBoostEvent(socket: Socket.Event) : SocketEvent(socket) {
    override fun handler(event: Array<Any>) {
        launch {
            val newBoost = GsonUtils.gson.fromJsonOrNull(event[0].toString(), NewBoost::class.java) ?: return@launch
            val toolsPlayer = Database.getUser(newBoost.discordId).await()
            if (toolsPlayer !== null) {
                val player = Bukkit.getPlayer(UUID.fromString(toolsPlayer.uuid))
                Database.addBoost(
                    userId = toolsPlayer.id,
                    amount = newBoost.amount,
                    isReceivedRewards = player !== null
                ).await()

                Bukkit.getPlayer(UUID.fromString(toolsPlayer.uuid))?.let {
                    GameToolsManager.giveBoostRewards(it, newBoost.amount)
                }
            }
            Utils.announce(
                messageConfig.boost.broadcastNewBoost,
                Placeholder.parsed("player", toolsPlayer?.username ?: newBoost.discordDisplayName),
                Placeholder.parsed("amount", newBoost.amount.toString()),
                Placeholder.parsed("server", newBoost.serverName),
                Placeholder.parsed("server_rank", newBoost.serverBoosts.toString()),
                Placeholder.parsed("server_boosts", newBoost.serverBoosts.toString()),
                Placeholder.parsed("url", "https://game-tools.ir/mc/servers/${newBoost.serverName.lowercase()}"),
            )
        }
    }
}