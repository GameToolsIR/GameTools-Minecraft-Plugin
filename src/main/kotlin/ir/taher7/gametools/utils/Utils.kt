package ir.taher7.gametools.utils

import ir.taher7.gametools.config.messageConfig
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.sayandev.stickynote.bukkit.extension.sendComponent
import java.util.*

object Utils {

    private val commandCoolDown = hashMapOf<UUID, Long>()

    fun checkCoolDown(player: Player): Boolean {
        val lastUsed = commandCoolDown[player.uniqueId]
        val colDownDirection = 5000
        if (lastUsed != null && (System.currentTimeMillis() - lastUsed) <= colDownDirection) {
            val remainingTime = (colDownDirection - (System.currentTimeMillis() - lastUsed)) / 1000
            player.sendComponent(messageConfig.general.coolDown, Placeholder.parsed("time", remainingTime.toString()))
            return true
        }
        return false
    }

    fun resetPlayerCoolDown(player: Player) {
        commandCoolDown[player.uniqueId] = System.currentTimeMillis()
    }

    fun removePlayerCoolDown(player: Player) {
        commandCoolDown.remove(player.uniqueId)
    }

    fun announce(message: String, vararg placeholder: TagResolver) {
        for (player in Bukkit.getOnlinePlayers()) {
            player.sendComponent(message, *placeholder)
        }
    }

    fun announce(message: List<String>, vararg placeholder: TagResolver) {
        for (player in Bukkit.getOnlinePlayers()) {
            for (msg in message) {
                player.sendComponent(msg, *placeholder)
            }
        }
    }
}