package ir.taher7.gametools.command.literals

import ir.taher7.gametools.command.Literal
import ir.taher7.gametools.core.GameToolsManager
import org.bukkit.entity.Player
import org.incendo.cloud.bukkit.parser.PlayerParser
import org.incendo.cloud.context.CommandContext
import org.incendo.cloud.description.CommandDescription
import org.incendo.cloud.kotlin.MutableCommandBuilder
import org.incendo.cloud.minecraft.extras.MinecraftHelp
import org.sayandev.stickynote.bukkit.command.BukkitSender
import org.sayandev.stickynote.bukkit.command.literalWithPermission
import org.sayandev.stickynote.bukkit.command.platformSender
import org.sayandev.stickynote.bukkit.extension.sendComponent

class RewardCommand(
    command: MutableCommandBuilder<BukkitSender>,
    private val help: MinecraftHelp<BukkitSender>
) : Literal(command, "reward") {

    init {

        commandBuilder.registerCopy {
            literalWithPermission("vote")
            required("player", PlayerParser.playerParser())
            handler { context ->
                val targetPlayer = context.get<Player>("player")
                GameToolsManager.giveVoteRewards(targetPlayer)
            }
        }

        commandBuilder.registerCopy {
            literalWithPermission("boost")
            required("player", PlayerParser.playerParser())
            commandDescription(CommandDescription.commandDescription("Give boost rewards to a player"))
            handler { context ->
                val targetPlayer = context.get<Player>("player")
                GameToolsManager.giveBoostRewards(targetPlayer, 3)
                context.platformSender().sendComponent("You have given boost rewards to ${targetPlayer.name}")
            }
        }

    }

    override fun handler(context: CommandContext<BukkitSender>) {
        help.queryCommands("", context.sender())
    }

}