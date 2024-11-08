package ir.taher7.gametools

import ir.taher7.gametools.command.CommandManager
import ir.taher7.gametools.database.Database
import ir.taher7.gametools.config.databaseConfig
import ir.taher7.gametools.config.messageConfig
import ir.taher7.gametools.config.settingConfig
import org.bukkit.plugin.java.JavaPlugin
import org.sayandev.stickynote.bukkit.hook.PlaceholderAPIHook
import org.sayandev.stickynote.loader.bukkit.StickyNoteBukkitLoader

class GameTools : JavaPlugin() {

    override fun onEnable() {
        StickyNoteBukkitLoader(this)
        PlaceholderAPIHook.injectComponent()
        instance = this

        messageConfig
        databaseConfig
        settingConfig
        Database
        CommandManager()
    }

    override fun onDisable() {

    }

    companion object {
        lateinit var instance: GameTools
    }

}