package ir.taher7.gametools.command.literals

import ir.taher7.gametools.command.Literal
import ir.taher7.gametools.config.messageConfig
import ir.taher7.gametools.core.GameToolsManager
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.entity.Player
import org.incendo.cloud.bukkit.parser.PlayerParser
import org.incendo.cloud.context.CommandContext
import org.incendo.cloud.description.CommandDescription
import org.incendo.cloud.kotlin.MutableCommandBuilder
import org.incendo.cloud.minecraft.extras.MinecraftHelp
import org.incendo.cloud.parser.standard.IntegerParser
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
                context.platformSender().sendComponent(
                    messageConfig.vote.giveReward,
                    Placeholder.parsed("player", targetPlayer.name)
                )
            }
        }

        commandBuilder.registerCopy {
            literalWithPermission("boost")
            required("player", PlayerParser.playerParser())
            required("amount", IntegerParser.integerParser())
            commandDescription(CommandDescription.commandDescription("Give boost rewards to player"))
            handler { context ->
                val targetPlayer = context.get<Player>("player")
                val amount = context.get<Int>("amount")
                GameToolsManager.giveBoostRewards(targetPlayer, amount)
                context.platformSender().sendComponent(
                    messageConfig.boost.giveReward,
                    Placeholder.parsed("player", targetPlayer.name),
                    Placeholder.parsed("amount", amount.toString())
                )
            }
        }

    }

    override fun handler(context: CommandContext<BukkitSender>) {
        help.queryCommands("", context.sender())
    }

}