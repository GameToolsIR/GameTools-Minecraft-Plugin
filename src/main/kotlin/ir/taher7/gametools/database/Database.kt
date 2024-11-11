package ir.taher7.gametools.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import ir.taher7.gametools.config.DatabaseStorage
import ir.taher7.gametools.config.databaseConfig
import ir.taher7.gametools.database.models.Vote
import kotlinx.coroutines.Deferred
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.experimental.suspendedTransactionAsync
import org.jetbrains.exposed.sql.transactions.transaction
import org.sayandev.stickynote.bukkit.plugin
import org.sayandev.stickynote.bukkit.pluginDirectory
import org.sayandev.stickynote.core.coroutine.dispatcher.AsyncDispatcher
import java.util.*

object Database {

    private val databaseDispatcher = AsyncDispatcher(
        "${plugin.name.lowercase()}-${databaseConfig.method}-thread",
        databaseConfig.poolSize
    )
    private val database: Database

    init {
        val config = HikariConfig().apply {
            jdbcUrl = when (databaseConfig.method) {
                DatabaseStorage.DatabaseMethod.H2 -> "jdbc:h2:file:${pluginDirectory.absolutePath}/storage"
                DatabaseStorage.DatabaseMethod.MYSQL,
                DatabaseStorage.DatabaseMethod.MARIADB -> "jdbc:${databaseConfig.method.name.lowercase()}://${databaseConfig.host}:${databaseConfig.port}/${databaseConfig.database}"
            }

            driverClassName = when (databaseConfig.method) {
                DatabaseStorage.DatabaseMethod.H2 -> "org.h2.Driver"
                DatabaseStorage.DatabaseMethod.MYSQL -> "com.mysql.cj.jdbc.Driver"
                DatabaseStorage.DatabaseMethod.MARIADB -> "org.mariadb.jdbc.Driver"
            }

            username = databaseConfig.username
            password = databaseConfig.password
            maximumPoolSize = databaseConfig.poolSize
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
                result[isReceivedRewards] = vote.isReceivedRewards
            }
        }
    }

    suspend fun getVote(uniqueId: UUID): Deferred<Vote?> {
        return async {
            Vote.Table
                .selectAll()
                .where { Vote.Table.uuid eq uniqueId.toString() }
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
            Vote.Table.deleteWhere { uuid eq uniqueId.toString() }
        }
    }

    suspend fun deleteVote(discordId: String): Deferred<Unit> {
        return async {
            Vote.Table.deleteWhere { Vote.Table.discordId eq discordId }
        }
    }

    private fun createVote(row: ResultRow): Vote {
        return Vote(
            row[Vote.Table.id],
            row[Vote.Table.uuid],
            row[Vote.Table.username],
            row[Vote.Table.discordId],
            row[Vote.Table.isReceivedRewards],
            row[Vote.Table.votedAt]
        )
    }

    suspend fun updateVote(discordId: String, vote: Vote): Deferred<Unit> {
        return async {
            Vote.Table.update({ Vote.Table.discordId eq discordId }) {
                it[isReceivedRewards] = vote.isReceivedRewards
            }
        }
    }

    suspend fun updateVote(uuid: UUID): Deferred<Unit> {
        return async {
            Vote.Table.update({ Vote.Table.uuid eq uuid.toString() }) {
                it[isReceivedRewards] = true
            }
        }
    }

    suspend fun <T> async(statement: suspend Transaction.() -> T): Deferred<T> {
        return suspendedTransactionAsync(
            databaseDispatcher,
            database,
            statement = statement
        )
    }

}