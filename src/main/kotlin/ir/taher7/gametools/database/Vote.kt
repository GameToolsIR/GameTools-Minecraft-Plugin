package ir.taher7.gametools.database

import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentTimestamp
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.sayandev.stickynote.bukkit.plugin
import java.util.UUID


data class Vote(
    val id: Int,
    val uuid: UUID,
    val username: String,
    val discordId: String,
    val votedAt: Instant,
) {

    object Table : IntIdTable("${plugin.name.lowercase()}_votes") {
        val uuid = uuid("uuid").uniqueIndex()
        val username = varchar("username", 32)
        val discordId = varchar("discordId", 32).uniqueIndex()
        val votedAt = timestamp("votedAt").defaultExpression(CurrentTimestamp)
    }

}
