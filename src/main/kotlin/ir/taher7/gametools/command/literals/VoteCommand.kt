package ir.taher7.gametools.command.literals

import ir.taher7.gametools.command.Literal
import ir.taher7.gametools.database.Database
import ir.taher7.gametools.database.Vote
import kotlinx.datetime.Clock
import org.incendo.cloud.context.CommandContext
import org.incendo.cloud.kotlin.MutableCommandBuilder
import org.sayandev.stickynote.bukkit.command.BukkitSender
import org.sayandev.stickynote.bukkit.command.player
import org.sayandev.stickynote.bukkit.extension.sendComponent
import org.sayandev.stickynote.bukkit.launch

class VoteCommand(
    command: MutableCommandBuilder<BukkitSender>
) : Literal(command, "vote") {
    override fun handler(context: CommandContext<BukkitSender>) {
        launch {
            val player = context.player() ?: return@launch

            val isVoted = Database.getVote(player.uniqueId).await()
            if (isVoted != null) {
                player.sendComponent("<red>You have already voted!")
                return@launch
            }

            val newVote = Vote(
                id = 0,
                uuid = player.uniqueId,
                discordId = "",
                username = player.name,
                votedAt = Clock.System.now()
            )

            Database.addVote(newVote).await()
            player.sendComponent("<green>You have voted successfully!")
        }
    }
}