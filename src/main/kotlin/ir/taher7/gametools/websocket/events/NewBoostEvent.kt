package ir.taher7.gametools.websocket.events

import ir.taher7.gametools.utils.GsonUtils
import ir.taher7.gametools.utils.GsonUtils.fromJsonOrNull
import ir.taher7.gametools.websocket.Socket
import ir.taher7.gametools.websocket.SocketEvent
import ir.taher7.gametools.websocket.models.NewBoost
import org.bukkit.Bukkit
import org.sayandev.stickynote.bukkit.extension.sendComponent
import org.sayandev.stickynote.bukkit.log

class NewBoostEvent(socket: Socket.Event) : SocketEvent(socket) {
    override fun handler(event: Array<Any>) {
        val newBoost = GsonUtils.gson.fromJsonOrNull(event[0].toString(), NewBoost::class.java) ?: return
        log(newBoost.discordId)
        log(newBoost.discordDisplayName)
        log(newBoost.serverName)
        log(newBoost.amount.toString())
        log(newBoost.serverBoosts.toString())
        log(newBoost.serverBoostRank.toString())


        for (player in Bukkit.getOnlinePlayers()) {
            player.sendComponent("<yellow> ${newBoost.amount}x <green>boost <yellow>has been received from <green>${newBoost.discordDisplayName}<yellow>.")
        }

    }
}