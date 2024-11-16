package ir.taher7.gametools.api.events

import ir.taher7.gametools.database.models.User
import ir.taher7.gametools.websocket.models.NewBoost
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class NewBoostEvent (
    val boost: NewBoost,
    val user: User?,
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