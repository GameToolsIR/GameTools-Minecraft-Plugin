package ir.taher7.gametools.config

import org.sayandev.stickynote.bukkit.pluginDirectory
import org.sayandev.stickynote.core.configuration.Config
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import java.io.File

var settingConfig: SettingConfig = SettingConfig.fromConfig() ?: SettingConfig.defaultConfig()

@ConfigSerializable
data class SettingConfig(
    val apiKey: String = "",
) : Config(pluginDirectory, FILE_NAME) {


    init {
        load()
    }

    companion object {
        private const val FILE_NAME = "setting.yml"
        private val storageConfigFile = File(pluginDirectory, FILE_NAME)

        fun defaultConfig(): SettingConfig {
            return SettingConfig()
        }

        fun fromConfig(): SettingConfig? {
            return fromConfig<SettingConfig>(storageConfigFile)
        }

        fun reload() {
            settingConfig = fromConfig() ?: defaultConfig()
        }

    }


}