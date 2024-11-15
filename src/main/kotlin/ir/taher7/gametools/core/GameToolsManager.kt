package ir.taher7.gametools.core

import ir.taher7.gametools.config.messageConfig
import ir.taher7.gametools.config.settingConfig
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.sayandev.stickynote.bukkit.extension.sendComponent
import org.sayandev.stickynote.bukkit.log

object GameToolsManager {
    fun giveVoteRewards(player: Player) {
        player.sendComponent(
            messageConfig.vote.newVote,
            Placeholder.parsed("player", player.name)
        )

        for (reward in settingConfig.vote.rewards) {
            log(reward.command)
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), reward.command.replace("<player>", player.name))
            reward.message?.let { player.sendComponent(it) }
        }
    }

    fun giveBoostRewards(player: Player, amount: Int) {
        player.sendComponent(
            messageConfig.boost.newBoost,
            Placeholder.parsed("amount", amount.toString()),
            Placeholder.parsed("player", player.name),
        )
        for (reward in settingConfig.boost.rewards) {
            Bukkit.dispatchCommand(
                Bukkit.getConsoleSender(),
                reward.command
                    .replace("<player>", player.name)
                    .replace("<amount>", amount.toString())
            )
            reward.message?.let {
                player.sendComponent(
                    it,
                    Placeholder.parsed("amount", amount.toString()),
                    Placeholder.parsed("player", player.name),
                )
            }
        }
    }
}
