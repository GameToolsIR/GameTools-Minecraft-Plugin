package ir.taher7.gametools.storage

import org.sayandev.stickynote.bukkit.plugin
import org.sayandev.stickynote.bukkit.pluginDirectory
import org.sayandev.stickynote.core.configuration.Config
import org.spongepowered.configurate.objectmapping.meta.Setting
import java.io.File

data class DatabaseStorage(
    val method: DatabaseMethod = DatabaseMethod.MARIADB,
    val host: String = "localhost",
    val port: Int = 3306,
    val username: String = "root",
    val password: String = "",
//    val database: String = plugin.name.lowercase(),
    val database: String = "melody",
    @Setting("use-ssl") val useSSL: Boolean = false,
    val poolSize: Int = 5
) : Config(pluginDirectory, FILE_NAME) {

    enum class DatabaseMethod {
        H2,
        MYSQL,
        MARIADB
    }

    companion object {
        private const val FILE_NAME = "storage.yml"
        private val storageConfigFile = File(pluginDirectory, FILE_NAME)
        private var storageConfig = fromConfig() ?: defaultConfig()

        fun defaultConfig(): DatabaseStorage {
            return DatabaseStorage()
        }

        fun fromConfig(): DatabaseStorage? {
            return fromConfig<DatabaseStorage>(storageConfigFile)
        }

        fun get(): DatabaseStorage {
            return storageConfig
        }

        fun reload() {
            storageConfig = fromConfig() ?: defaultConfig()
        }
    }

}