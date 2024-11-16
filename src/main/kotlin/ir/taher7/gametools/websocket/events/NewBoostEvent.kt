package ir.taher7.gametools.websocket.events

import ir.taher7.gametools.api.events.NewBoostEvent
import ir.taher7.gametools.config.messageConfig
import ir.taher7.gametools.config.settingConfig
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
            if (!settingConfig.boost.enable) return@launch
            val newBoost = GsonUtils.gson.fromJsonOrNull(event[0].toString(), NewBoost::class.java) ?: return@launch
            val user = Database.getUser(newBoost.discordId).await()

            val newBoostEvent = NewBoostEvent(newBoost, user)
            Bukkit.getPluginManager().callEvent(newBoostEvent)
            if (newBoostEvent.isCancelled) return@launch

            if (newBoostEvent.user !== null) {
                val player = Bukkit.getPlayer(UUID.fromString(newBoostEvent.user.uuid))
                Database.addBoost(newBoostEvent.user.id, newBoostEvent.boost.amount, player !== null).await()
                Bukkit.getPlayer(UUID.fromString(newBoostEvent.user.uuid))?.let {
                    GameToolsManager.giveBoostRewards(it, newBoostEvent.boost.amount)
                }
            }

            if (!settingConfig.boost.broadcastMessage) return@launch
            if (settingConfig.boost.onlyBroadcastExistPlayerMessage && newBoostEvent.user === null) return@launch

            Utils.announce(
                messageConfig.boost.broadcastNewBoost,
                Placeholder.parsed("player", newBoostEvent.user?.username ?: newBoostEvent.boost.discordDisplayName),
                Placeholder.parsed("amount", newBoostEvent.boost.amount.toString()),
                Placeholder.parsed("server_rank", newBoostEvent.boost.serverBoostRank.toString()),
                Placeholder.parsed("server_boosts", newBoostEvent.boost.serverBoosts.toString()),
                Placeholder.parsed(
                    "url",
                    "https://game-tools.ir/mc/servers/${GameToolsManager.serverData.name.lowercase()}"
                ),
            )
        }
    }
}