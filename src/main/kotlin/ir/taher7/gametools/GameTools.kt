package ir.taher7.gametools

import ir.taher7.gametools.command.CommandManager
import ir.taher7.gametools.config.messageConfig
import ir.taher7.gametools.config.settingConfig
import ir.taher7.gametools.config.storageConfig
import ir.taher7.gametools.database.Database
import ir.taher7.gametools.listeners.GameToolsListener
import ir.taher7.gametools.schedulers.AutoAnnounce
import ir.taher7.gametools.utils.Utils
import ir.taher7.gametools.websocket.Socket
import org.bukkit.plugin.java.JavaPlugin
import org.sayandev.stickynote.bukkit.hook.PlaceholderAPIHook
import org.sayandev.stickynote.loader.bukkit.StickyNoteBukkitLoader

class GameTools : JavaPlugin() {

    override fun onEnable() {
        // Load StickyNote
        StickyNoteBukkitLoader(this)
        PlaceholderAPIHook.injectComponent()


        // Set instance
        instance = this


        // Load configs
        messageConfig
        storageConfig
        settingConfig


        // Connect to
        Database
        Socket


        // Register schedulers
        AutoAnnounce


        // Register commands
        CommandManager()


        // Register listeners
        server.pluginManager.registerEvents(GameToolsListener(), this)


        // Register tag resolver
        Utils.initTagResolvers()
    }

    override fun onDisable() {
        Socket.close()
    }

    companion object {
        lateinit var instance: GameTools
    }

}