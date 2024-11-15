package ir.taher7.gametools.database.models

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentTimestamp
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.sayandev.stickynote.bukkit.plugin


data class Vote(
    val id: Int = 0,
    val userId: Int = 0,
    val isReceivedRewards: Boolean = false,
    val votedAt: Instant = Clock.System.now(),
) {

    object Table : org.jetbrains.exposed.sql.Table("${plugin.name.lowercase()}_votes") {
        val id = integer("id").autoIncrement()
        val userId = integer("userId").references(User.Table.id, ReferenceOption.CASCADE, ReferenceOption.CASCADE).uniqueIndex()
        val isReceivedRewards = bool("isReceivedRewards")
        val votedAt = timestamp("votedAt").defaultExpression(CurrentTimestamp)

        override val primaryKey = PrimaryKey(id)

    }


}
