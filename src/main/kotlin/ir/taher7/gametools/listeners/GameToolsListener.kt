package ir.taher7.gametools.listeners

import ir.taher7.gametools.core.GameToolsManager
import ir.taher7.gametools.database.Database
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.sayandev.stickynote.bukkit.launch

class GameToolsListener : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        launch {
            val player = event.player
            val existVote = Database.getVote(player.uniqueId).await() ?: return@launch
            if (!existVote.isReceivedRewards) {
                GameToolsManager.giveVoteRewards(player)
                Database.updateVote(player.uniqueId).await()
            }
        }
    }
}