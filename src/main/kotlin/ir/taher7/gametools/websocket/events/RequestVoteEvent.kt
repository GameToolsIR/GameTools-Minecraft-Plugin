package ir.taher7.gametools.websocket.events

import ir.taher7.gametools.config.messageConfig
import ir.taher7.gametools.core.GameToolsManager
import ir.taher7.gametools.utils.GsonUtils
import ir.taher7.gametools.utils.GsonUtils.fromJsonOrNull
import ir.taher7.gametools.websocket.Socket
import ir.taher7.gametools.websocket.SocketEvent
import ir.taher7.gametools.websocket.models.RequestVote
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Bukkit
import org.sayandev.stickynote.bukkit.extension.sendComponent
import java.util.*

class RequestVoteEvent(event: Socket.Event) : SocketEvent(event) {
    override fun handler(event: Array<Any>) {
        val requestVote = GsonUtils.gson.fromJsonOrNull(event[0].toString(), RequestVote::class.java) ?: return
        val player = Bukkit.getPlayer(UUID.fromString(requestVote.uuid)) ?: return
        player.sendComponent(
            messageConfig.vote.vote,
            Placeholder.parsed("url", "https://game-tools.ir/mc/servers/${GameToolsManager.serverData.name.lowercase()}?voteToken=${requestVote.token}"),
            Placeholder.parsed("server", GameToolsManager.serverData.name),
        )
    }
}