package ir.taher7.gametools

import ir.taher7.gametools.command.CommandManager
import ir.taher7.gametools.database.Database
import ir.taher7.gametools.config.databaseConfig
import ir.taher7.gametools.config.messageConfig
import ir.taher7.gametools.config.settingConfig
import ir.taher7.gametools.listeners.GameToolsListener
import ir.taher7.gametools.websocket.Socket
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
        Socket
        CommandManager()


        server.pluginManager.registerEvents(GameToolsListener(), this)
    }

    override fun onDisable() {
        Socket.close()
    }

    companion object {
        lateinit var instance: GameTools
    }

}