package ir.taher7.gametools.config

import org.sayandev.stickynote.bukkit.pluginDirectory
import org.sayandev.stickynote.core.configuration.Config
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import java.io.File

var messageConfig: MessageConfig = MessageConfig.fromConfig() ?: MessageConfig.defaultConfig()

@ConfigSerializable
data class MessageConfig(
    val general: General = General()
) : Config(pluginDirectory, FILE_NAME) {
    init {
        load()
    }


    @ConfigSerializable
    data class General(
        val reload: String = "<green>Reloading configuration... <player>"
    )

    companion object {
        private const val FILE_NAME = "message.yml"
        private val storageConfigFile = File(pluginDirectory, FILE_NAME)

        fun defaultConfig(): MessageConfig {
            return MessageConfig()
        }

        fun fromConfig(): MessageConfig? {
            return fromConfig<MessageConfig>(storageConfigFile)
        }

        fun reload() {
            messageConfig = fromConfig() ?: defaultConfig()
        }

    }


}