package ir.taher7.gametools.schedulers

import ir.taher7.gametools.config.messageConfig
import ir.taher7.gametools.config.settingConfig
import ir.taher7.gametools.core.GameToolsManager
import ir.taher7.gametools.utils.Utils
import ir.taher7.gametools.websocket.Socket
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import org.sayandev.stickynote.bukkit.plugin

object AutoAnnounce {
    private var voteRunnable: BukkitTask
    private var boostRunnable: BukkitTask

    init {
        voteRunnable = voteAutoAnnounce()
        boostRunnable = boostAutoAnnounce()
    }


    private fun voteAutoAnnounce(): BukkitTask {
        return object : BukkitRunnable() {
            override fun run() {
                if (!settingConfig.vote.enable) return
                if (!settingConfig.vote.autoAnnounce.enable) return
                if (!Socket.isActive()) return
                Utils.announce(
                    messageConfig.vote.autoAnnounce,
                    Placeholder.parsed("server", GameToolsManager.serverData.name)
                )
            }
        }.runTaskTimerAsynchronously(
            plugin,
            (settingConfig.vote.autoAnnounce.interval * 20),
            (settingConfig.vote.autoAnnounce.interval * 20)
        )
    }

    private fun boostAutoAnnounce(): BukkitTask {
        return object : BukkitRunnable() {
            override fun run() {
                if (!settingConfig.boost.enable) return
                if (!settingConfig.boost.autoAnnounce.enable) return
                if (!Socket.isActive()) return
                Utils.announce(
                    messageConfig.boost.autoAnnounce,
                    Placeholder.parsed("server", GameToolsManager.serverData.name),
                    Placeholder.parsed(
                        "url",
                        "https://game-tools.ir/mc/servers/${GameToolsManager.serverData.name.lowercase()}"
                    ),
                )
            }
        }.runTaskTimerAsynchronously(
            plugin,
            (settingConfig.boost.autoAnnounce.interval * 20),
            (settingConfig.boost.autoAnnounce.interval * 20)
        )
    }

    private fun cancel() {
        voteRunnable.cancel()
        boostRunnable.cancel()
    }

    private fun start() {
        voteRunnable = voteAutoAnnounce()
        boostRunnable = boostAutoAnnounce()
    }

    fun restart() {
        cancel()
        start()
    }
}