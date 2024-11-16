package ir.taher7.gametools.config

import org.sayandev.stickynote.bukkit.plugin
import org.sayandev.stickynote.bukkit.pluginDirectory
import org.sayandev.stickynote.core.configuration.Config
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Setting
import java.io.File

val storageConfig: StorageConfig = StorageConfig.fromConfig() ?: StorageConfig.defaultConfig()

@ConfigSerializable
data class StorageConfig(
    val database: Database = Database(),
    val websocket: Websocket = Websocket()
) : Config(pluginDirectory, FILE_NAME) {

    init {
        load()
    }

    enum class DatabaseMethod {
        SQLITE,
        MYSQL,
        MARIADB
    }

    @ConfigSerializable
    data class Database(
        val method: DatabaseMethod = DatabaseMethod.SQLITE,
        val host: String = "localhost",
        val port: Int = 3306,
        val username: String = "root",
        val password: String = "",
        val database: String = plugin.name.lowercase(),
        @Setting("use-ssl") val useSSL: Boolean = false,
        val poolSize: Int = 5
    )

    @ConfigSerializable
    data class Websocket(
        val token: String = "",
        val host: String = "app.game-tools.ir",
        val port: Int = 443,
        @Setting("use-ssl") val useSSL: Boolean = true,
    )

    companion object {
        private const val FILE_NAME = "config.yml"
        private val databaseConfigFile = File(pluginDirectory, FILE_NAME)

        fun defaultConfig(): StorageConfig {
            return StorageConfig()
        }

        fun fromConfig(): StorageConfig? {
            return fromConfig<StorageConfig>(databaseConfigFile)
        }

//        fun reload() {
//            databaseStorage = fromConfig() ?: defaultConfig()
//        }
    }

}