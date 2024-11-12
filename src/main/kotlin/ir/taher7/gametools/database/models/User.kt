package ir.taher7.gametools.database.models

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentTimestamp
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.sayandev.stickynote.bukkit.plugin

data class User(
    val id: Int = 0,
    val uuid: String,
    val username: String,
    val discordId: String,
    val createdAt: Instant = Clock.System.now(),
) {

    object Table : org.jetbrains.exposed.sql.Table("${plugin.name.lowercase()}_users") {
        val id = integer("id").autoIncrement()
        val uuid = varchar("uuid", 36).uniqueIndex()
        val username = varchar("username", 32)
        val discordId = varchar("discordId", 32).uniqueIndex()
        val createdAt = timestamp("createdAt").defaultExpression(CurrentTimestamp)

        override val primaryKey = PrimaryKey(id)
    }

}