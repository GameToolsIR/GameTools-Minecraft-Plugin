package ir.taher7.gametools.utils

import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.Bukkit
import org.sayandev.stickynote.bukkit.extension.sendComponent

object Utils {
    fun announce(message: String, vararg placeholder: TagResolver) {
        for (player in Bukkit.getOnlinePlayers()) {
            player.sendComponent(message, *placeholder)
        }
    }
}