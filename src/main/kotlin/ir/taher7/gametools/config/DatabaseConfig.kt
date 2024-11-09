package ir.taher7.gametools.config

import org.sayandev.stickynote.bukkit.plugin
import org.sayandev.stickynote.bukkit.pluginDirectory
import org.sayandev.stickynote.core.configuration.Config
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Setting
import java.io.File

val databaseConfig: DatabaseStorage = DatabaseStorage.fromConfig() ?: DatabaseStorage.defaultConfig()

@ConfigSerializable
data class DatabaseStorage(
    val method: DatabaseMethod = DatabaseMethod.H2,
    val host: String = "localhost",
    val port: Int = 3306,
    val username: String = "root",
    val password: String = "",
    val database: String = plugin.name.lowercase(),
    @Setting("use-ssl") val useSSL: Boolean = false,
    val poolSize: Int = 5
): Config(pluginDirectory, FILE_NAME) {

    init {
        load()
    }

    enum class DatabaseMethod {
        H2,
        MYSQL,
        MARIADB
    }

    companion object {
        private const val FILE_NAME = "storage.yml"
        private val databaseConfigFile = File(pluginDirectory, FILE_NAME)

        fun defaultConfig(): DatabaseStorage {
            return DatabaseStorage()
        }

        fun fromConfig(): DatabaseStorage? {
            return fromConfig<DatabaseStorage>(databaseConfigFile)
        }

//        fun reload() {
//            databaseStorage = fromConfig() ?: defaultConfig()
//        }
    }

}