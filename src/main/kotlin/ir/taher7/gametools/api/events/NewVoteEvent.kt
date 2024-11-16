package ir.taher7.gametools.api.events

import ir.taher7.gametools.database.models.Vote
import ir.taher7.gametools.websocket.models.NewVote
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class NewVoteEvent(
    val vote: NewVote,
    val isVoted: Vote?,
    val player: Player?,
) : Event(), Cancellable {
    private var cancelled = false
    override fun isCancelled(): Boolean {
        return cancelled
    }

    override fun setCancelled(cancel: Boolean) {
        cancelled = cancel
    }

    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }
}