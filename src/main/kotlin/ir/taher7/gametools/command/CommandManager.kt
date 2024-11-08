package ir.taher7.gametools.command

import ir.taher7.gametools.command.literals.ReloadCommand
import ir.taher7.gametools.command.literals.VoteCommand
import org.incendo.cloud.context.CommandContext
import org.sayandev.stickynote.bukkit.command.BukkitCommand
import org.sayandev.stickynote.bukkit.command.BukkitSender

class CommandManager : BukkitCommand(
    "gametools",
    "gt"
) {

    override fun rootHandler(context: CommandContext<BukkitSender>) {
        help.queryCommands("", context.sender())
    }

    init {
        /*
        /gametools vote
        /gametools reload
         */
        registerHelpLiteral()
        VoteCommand(rawCommandBuilder())
        ReloadCommand(rawCommandBuilder())
    }

}