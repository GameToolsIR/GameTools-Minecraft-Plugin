package ir.taher7.gametools.command.literals

import ir.taher7.gametools.command.Literal
import ir.taher7.gametools.database.Database
import ir.taher7.gametools.websocket.Socket
import org.incendo.cloud.context.CommandContext
import org.incendo.cloud.kotlin.MutableCommandBuilder
import org.sayandev.stickynote.bukkit.command.BukkitSender
import org.sayandev.stickynote.bukkit.command.player
import org.sayandev.stickynote.bukkit.extension.sendComponent
import org.sayandev.stickynote.bukkit.launch
import org.sayandev.stickynote.bukkit.log

class VoteCommand(
    command: MutableCommandBuilder<BukkitSender>
) : Literal(command, "vote") {
    override fun handler(context: CommandContext<BukkitSender>) {
        launch {
            val player = context.player() ?: return@launch

            val isVoted = Database.getVote(player.uniqueId).await()
            if (isVoted != null) {
                player.sendComponent("<red>You have already voted! ${isVoted.uuid}")
                return@launch
            }

            if (!Socket.isActive()) {
                player.sendComponent("<red>Something went wrong! Please try again later.")
                return@launch
            }


            Socket.emit(
                "requestVote",
                mapOf(
                    "uuid" to player.uniqueId.toString(),
                    "ip" to  player.address.address.hostAddress,
                    "username" to player.name
                )
            )

        }
    }
}