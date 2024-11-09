package ir.taher7.gametools.config

import org.sayandev.stickynote.bukkit.pluginDirectory
import org.sayandev.stickynote.core.configuration.Config
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Setting
import java.io.File

var settingConfig: SettingConfig = SettingConfig.fromConfig() ?: SettingConfig.defaultConfig()

@ConfigSerializable
data class SettingConfig(
    val websocket: Websocket = Websocket()
) : Config(pluginDirectory, FILE_NAME) {

    init {
        load()
    }

    @ConfigSerializable
    data class Websocket(
        val token: String = "",
        val host: String = "app.game-tools.ir",
        val port: Int = 443,
        @Setting("use-ssl") val useSSL: Boolean = true,
    )

    companion object {
        private const val FILE_NAME = "setting.yml"
        private val settingConfigFile = File(pluginDirectory, FILE_NAME)

        fun defaultConfig(): SettingConfig {
            return SettingConfig()
        }

        fun fromConfig(): SettingConfig? {
            return fromConfig<SettingConfig>(settingConfigFile)
        }

        fun reload() {
            settingConfig = fromConfig() ?: defaultConfig()
        }

    }


}