package ir.taher7.gametools.command

import org.incendo.cloud.context.CommandContext
import org.incendo.cloud.kotlin.MutableCommandBuilder
import org.sayandev.stickynote.bukkit.command.BukkitSender
import org.sayandev.stickynote.bukkit.command.literalWithPermission

abstract class Literal(
    commandBuilder: MutableCommandBuilder<BukkitSender>,
    name: String
) {

    init {
        commandBuilder.registerCopy {
            literalWithPermission(name)
            builder(commandBuilder)
            handler { context -> handler(context) }
        }
    }

    abstract fun handler(context: CommandContext<BukkitSender>)

    open fun builder(commandBuilder: MutableCommandBuilder<BukkitSender>) {

    }

}