package ir.taher7.gametools.listeners

import ir.taher7.gametools.core.GameToolsManager
import ir.taher7.gametools.database.Database
import ir.taher7.gametools.utils.Utils
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.sayandev.stickynote.bukkit.launch

class GameToolsListener : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        launch {
            val player = event.player
            val toolsPlayer = Database.getUser(player.uniqueId).await() ?: return@launch

            val existVote = Database.getVote(toolsPlayer.id, Database.HandleGetType.ID).await()
            if (existVote !== null && !existVote.isReceivedRewards) {
                GameToolsManager.giveVoteRewards(player)
                Database.updateVote(existVote.id, Database.HandleGetType.ID, true).await()
            }

            val existBoost = Database.getUserBoosts(toolsPlayer.id).await()
            if (existBoost.isNotEmpty()) {
                for (boost in existBoost) {
                    if (!boost.isReceivedRewards) {
                        GameToolsManager.giveBoostRewards(player, boost.amount)
                        Database.updateBoost(boost.id, Database.HandleGetType.ID, true).await()
                    }
                }
            }
        }
    }


    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        Utils.removePlayerCoolDown(event.player)
    }
}