package ir.taher7.gametools.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import ir.taher7.gametools.config.DatabaseStorage
import ir.taher7.gametools.config.databaseConfig
import ir.taher7.gametools.database.models.Boost
import ir.taher7.gametools.database.models.User
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
            SchemaUtils.create(User.Table)
            SchemaUtils.create(Vote.Table)
            SchemaUtils.create(Boost.Table)
        }
    }

    private fun createUser(row: ResultRow): User {
        return User(
            row[User.Table.id],
            row[User.Table.uuid],
            row[User.Table.username],
            row[User.Table.discordId],
            row[User.Table.createdAt]
        )
    }

    suspend fun addUser(uuid: String, username: String, discordId: String): Deferred<User> {
        return async {
            val user = User.Table.insert { result ->
                result[User.Table.uuid] = uuid
                result[User.Table.username] = username
                result[User.Table.discordId] = discordId
            }
            User(
                user[User.Table.id],
                user[User.Table.uuid],
                user[User.Table.username],
                user[User.Table.discordId],
            )
        }
    }

    suspend fun getUser(uuid: UUID): Deferred<User?> {
        return async {
            User.Table.selectAll().where { User.Table.uuid eq uuid.toString() }.firstOrNull()?.let { createUser(it) }
        }
    }

    suspend fun getUser(discordId: String): Deferred<User?> {
        return async {
            User.Table.selectAll().where { User.Table.discordId eq discordId }.firstOrNull()?.let { createUser(it) }
        }
    }


    private fun createVote(row: ResultRow): Vote {
        return Vote(
            row[Vote.Table.id],
            row[Vote.Table.userId],
            row[Vote.Table.isReceivedRewards],
        )
    }

    suspend fun addVote(vote: Vote): Deferred<Unit> {
        return async {
            Vote.Table.insert { result ->
                result[Vote.Table.userId] = vote.userId
                result[Vote.Table.isReceivedRewards] = vote.isReceivedRewards
            }
        }
    }

    suspend fun getVote(id: Int, type: HandleGetType): Deferred<Vote?> {
        return async {
            when (type) {
                HandleGetType.ID -> Vote.Table.selectAll().where { Vote.Table.id eq id }.firstOrNull()
                    ?.let { createVote(it) }

                HandleGetType.USER_ID -> Vote.Table.selectAll().where { Vote.Table.userId eq id }.firstOrNull()
                    ?.let { createVote(it) }
            }
        }
    }

    suspend fun updateVote(id: Int, type: HandleGetType, isReceivedRewards: Boolean): Deferred<Unit> {
        return async {
            when (type) {
                HandleGetType.ID -> Vote.Table.update({ Vote.Table.id eq id }) {
                    it[Vote.Table.isReceivedRewards] = isReceivedRewards
                }

                HandleGetType.USER_ID -> Vote.Table.update({ Vote.Table.userId eq id }) {
                    it[Vote.Table.isReceivedRewards] = isReceivedRewards
                }
            }
        }
    }

    suspend fun deleteVote(id: Int, type: HandleGetType): Deferred<Unit> {
        return async {
            when (type) {
                HandleGetType.ID -> Vote.Table.deleteWhere { Vote.Table.id eq id }
                HandleGetType.USER_ID -> Vote.Table.deleteWhere { Vote.Table.userId eq id }
            }
        }
    }

    private fun createBoost(row: ResultRow): Boost {
        return Boost(
            row[Boost.Table.id],
            row[Boost.Table.userId],
            row[Boost.Table.amount],
            row[Boost.Table.isReceivedRewards],
            row[Boost.Table.boostedAt]
        )
    }

    suspend fun addBoost(userId: Int, amount: Int, isReceivedRewards: Boolean): Deferred<Unit> {
        return async {
            Boost.Table.insert { result ->
                result[Boost.Table.userId] = userId
                result[Boost.Table.amount] = amount
                result[Boost.Table.isReceivedRewards] = isReceivedRewards
            }
        }
    }

    suspend fun getBoost(id: Int, type: HandleGetType): Deferred<Boost?> {
        return async {
            when (type) {
                HandleGetType.ID -> Boost.Table.selectAll().where { Boost.Table.id eq id }.firstOrNull()
                    ?.let { createBoost(it) }

                HandleGetType.USER_ID -> Boost.Table.selectAll().where { Boost.Table.userId eq id }.firstOrNull()
                    ?.let { createBoost(it) }
            }
        }
    }

    suspend fun getUserBoosts(userId: Int): Deferred<List<Boost>> {
        return async {
            Boost.Table.selectAll().where { Boost.Table.userId eq userId }.map { createBoost(it) }
        }
    }

    suspend fun updateBoost(id: Int, type: HandleGetType, isReceivedRewards: Boolean): Deferred<Unit> {
        return async {
            when (type) {
                HandleGetType.ID -> Boost.Table.update({ Boost.Table.id eq id }) {
                    it[Boost.Table.isReceivedRewards] = isReceivedRewards
                }

                HandleGetType.USER_ID -> Boost.Table.update({ Boost.Table.userId eq id }) {
                    it[Boost.Table.isReceivedRewards] = isReceivedRewards
                }
            }
        }
    }

    suspend fun deleteBoost(id: Int, type: HandleGetType): Deferred<Unit> {
        return async {
            when (type) {
                HandleGetType.ID -> Boost.Table.deleteWhere { Boost.Table.id eq id }
                HandleGetType.USER_ID -> Boost.Table.deleteWhere { Boost.Table.userId eq id }
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

    enum class HandleGetType {
        ID,
        USER_ID
    }


}