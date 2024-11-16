package ir.taher7.gametools.utils

import ir.taher7.gametools.config.messageConfig
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.sayandev.stickynote.bukkit.extension.sendComponent
import org.sayandev.stickynote.bukkit.utils.AdventureUtils
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

    fun initTagResolvers(vararg placeholder: TagResolver) {
        AdventureUtils.setTagResolver(
            Placeholder.parsed("prefix", messageConfig.general.prefix),
            Placeholder.parsed("hover_color", messageConfig.general.hoverColor),
            Placeholder.parsed("text_color", messageConfig.general.textColor),
            Placeholder.parsed("highlight_color", messageConfig.general.highlightColor),
            Placeholder.parsed("header", messageConfig.general.header),
            Placeholder.parsed("footer", messageConfig.general.footer),
            *placeholder,
        )
    }
}