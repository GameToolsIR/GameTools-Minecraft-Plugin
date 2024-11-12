package ir.taher7.gametools.core

import org.bukkit.entity.Player
import org.sayandev.stickynote.bukkit.extension.sendComponent

object GameToolsManager {
    fun giveVoteRewards(player: Player) {
        player.sendComponent("<yellow>Thank you for voting for the server and supporting us. <blue>Enjoy your rewards!")
        player.giveExp(1000)
        player.sendComponent("<green>+1000 <yellow>exp")
        player.sendComponent("<yellow> reward #1")
        player.sendComponent("<yellow> reward #2")
        player.sendComponent("<yellow> reward #3")
    }

    fun giveBoostRewards(player: Player, amount: Int) {
        player.sendComponent("<yellow>Thank you for boosting the server and supporting us. <blue>Enjoy your rewards!")
        player.giveExp(amount)
        player.sendComponent("<green>+$amount <yellow>exp")
        player.sendComponent("<yellow> reward #1")
        player.sendComponent("<yellow> reward #2")
        player.sendComponent("<yellow> reward #3")
    }
}
