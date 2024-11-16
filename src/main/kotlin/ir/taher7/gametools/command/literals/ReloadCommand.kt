package ir.taher7.gametools.command.literals

import ir.taher7.gametools.command.Literal
import ir.taher7.gametools.config.MessageConfig
import ir.taher7.gametools.config.SettingConfig
import ir.taher7.gametools.config.messageConfig
import ir.taher7.gametools.core.GameToolsManager
import ir.taher7.gametools.schedulers.AutoAnnounce
import ir.taher7.gametools.utils.Utils
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.incendo.cloud.context.CommandContext
import org.incendo.cloud.kotlin.MutableCommandBuilder
import org.sayandev.stickynote.bukkit.command.BukkitSender
import org.sayandev.stickynote.bukkit.extension.sendComponent

class ReloadCommand(
    command: MutableCommandBuilder<BukkitSender>
) : Literal(command, "reload") {

    override fun handler(context: CommandContext<BukkitSender>) {

        SettingConfig.reload()
        MessageConfig.reload()
        AutoAnnounce.restart()
        Utils.initTagResolvers(
            Placeholder.parsed("server", GameToolsManager.serverData.name),
            Placeholder.parsed("server_color", "<${GameToolsManager.serverData.color}>"),
        )
        context.sender().platformSender().sendComponent(
            messageConfig.general.reload,
            Placeholder.parsed("player", context.sender().platformSender().name)
        )

    }
}