package ir.taher7.gametools.command

import org.incendo.cloud.context.CommandContext
import org.incendo.cloud.kotlin.MutableCommandBuilder
import org.sayandev.stickynote.bukkit.command.BukkitSender
import org.sayandev.stickynote.bukkit.command.literalWithPermission

abstract class Literal(
    parentCommandBuilder: MutableCommandBuilder<BukkitSender>,
    name: String,
) {

    val commandBuilder: MutableCommandBuilder<BukkitSender>

    init {
        commandBuilder = parentCommandBuilder.registerCopy {
            literalWithPermission(name)
            builder(parentCommandBuilder)
            handler { context -> handler(context) }
        }
    }

    abstract fun handler(context: CommandContext<BukkitSender>)

    open fun builder(commandBuilder: MutableCommandBuilder<BukkitSender>) {

    }

}