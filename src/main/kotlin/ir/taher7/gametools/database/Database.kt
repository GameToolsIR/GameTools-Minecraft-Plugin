package ir.taher7.gametools.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import ir.taher7.gametools.storage.DatabaseStorage
import kotlinx.coroutines.Deferred
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync
import org.jetbrains.exposed.sql.transactions.transaction
import org.sayandev.stickynote.bukkit.log
import org.sayandev.stickynote.bukkit.plugin
import org.sayandev.stickynote.bukkit.pluginDirectory
import org.sayandev.stickynote.core.coroutine.dispatcher.AsyncDispatcher
import java.util.UUID

object Database {

    private val databaseDispatcher = AsyncDispatcher(
        "${plugin.name.lowercase()}-${DatabaseStorage.get().method}-thread",
        DatabaseStorage.get().poolSize
    )
    private val database: Database

    init {
        val config = HikariConfig().apply {
            jdbcUrl = when (DatabaseStorage.get().method) {
                DatabaseStorage.DatabaseMethod.H2 -> "jdbc:h2:file:${pluginDirectory.absolutePath}/storage"
                DatabaseStorage.DatabaseMethod.MYSQL,
                DatabaseStorage.DatabaseMethod.MARIADB -> "jdbc:${DatabaseStorage.get().method.name.lowercase()}:://${DatabaseStorage.get().host}:${DatabaseStorage.get().port}/${DatabaseStorage.get().database}"
            }

            driverClassName = when (DatabaseStorage.get().method) {
                DatabaseStorage.DatabaseMethod.H2 -> "org.h2.Driver"
                DatabaseStorage.DatabaseMethod.MYSQL -> "com.mysql.cj.jdbc.Driver"
                DatabaseStorage.DatabaseMethod.MARIADB -> "org.mariadb.jdbc.Driver"
            }

            username = DatabaseStorage.get().username
            password = DatabaseStorage.get().password
            maximumPoolSize = DatabaseStorage.get().poolSize
        }
        database = Database.connect(HikariDataSource(config))
        TransactionManager.defaultDatabase = database

        transaction {
            SchemaUtils.create(Vote.Table)
        }
    }

    suspend fun addVote(vote: Vote): Deferred<Unit> {
        return async {
            Vote.Table.insert { result ->
                result[uuid] = vote.uuid
                result[username] = vote.username
                result[discordId] = vote.discordId
            }
        }
    }

    suspend fun getVote(uniqueId: UUID): Deferred<Vote?> {
        log(uniqueId.toString())
        return async {
            Vote.Table
                .selectAll()
                .where { Vote.Table.uuid eq uniqueId }
                .firstOrNull()
                ?.let {
                    createVote(it)
                }
        }
    }

    suspend fun getVote(discordId: String): Deferred<Vote?> {
        return async {
            Vote.Table.selectAll().where { Vote.Table.discordId eq discordId }.firstOrNull()?.let {
                createVote(it)
            }
        }
    }

    suspend fun deleteVote(uniqueId: UUID): Deferred<Unit> {
        return async {
            Vote.Table.deleteWhere { uuid eq uniqueId }
        }
    }

    suspend fun deleteVote(discordId: String): Deferred<Unit> {
        return async {
            Vote.Table.deleteWhere { Vote.Table.discordId eq discordId }
        }
    }

    private fun createVote(row: ResultRow): Vote {
        log("Creating vote from row: $row")
        return Vote(
            row[Vote.Table.id].value,
            row[Vote.Table.uuid],
            row[Vote.Table.username],
            row[Vote.Table.discordId],
            row[Vote.Table.votedAt]
        )
    }

    suspend fun <T> async(statement: suspend Transaction.() -> T): Deferred<T> {
        return suspendedTransactionAsync(
            databaseDispatcher,
            database,
            statement = statement
        )
    }

}