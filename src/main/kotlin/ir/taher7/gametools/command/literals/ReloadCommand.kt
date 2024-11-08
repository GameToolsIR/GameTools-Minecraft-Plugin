package ir.taher7.gametools.command.literals

import ir.taher7.gametools.command.Literal
import org.incendo.cloud.context.CommandContext
import org.incendo.cloud.kotlin.MutableCommandBuilder
import org.sayandev.stickynote.bukkit.command.BukkitSender

class ReloadCommand(
    command: MutableCommandBuilder<BukkitSender>
) : Literal(command, "reload") {

    override fun handler(context: CommandContext<BukkitSender>) {

    }

}