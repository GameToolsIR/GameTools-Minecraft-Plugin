package ir.taher7.gametools.config

import org.sayandev.stickynote.bukkit.pluginDirectory
import org.sayandev.stickynote.core.configuration.Config
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import org.spongepowered.configurate.objectmapping.meta.Setting
import java.io.File

var settingConfig: SettingConfig = SettingConfig.fromConfig() ?: SettingConfig.defaultConfig()

@ConfigSerializable
data class SettingConfig(
    val websocket: Websocket = Websocket(),
    val vote: Vote = Vote(),
    val boost: Boost = Boost(),
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

    @ConfigSerializable
    data class Vote(
        val enable: Boolean = true,
        val broadcastMessage: Boolean = true,
        val onlyBroadcastExistPlayerMessage: Boolean = false,
        val getRewardJustInFirstTime: Boolean = false,
        val rewards: List<Reward> = listOf(
            Reward("give <player> diamond 1", "<prefix>You have received highlight_color1 diamond <text_color>for voting!"),
            Reward("give <player> emerald 1"),
            Reward("lp user <player> parent addtemp vip 3d", "<prefix>You have received <highlight_color>3 day <text_color>VIP rank for voting!"),
        )
    )


    @ConfigSerializable
    data class Boost(
        val enable: Boolean = true,
        val broadcastMessage: Boolean = true,
        val onlyBroadcastExistPlayerMessage: Boolean = false,
        val rewards: List<Reward> = listOf(
            Reward("give <player> diamond <amount>", "<prefix>You have received <highlight_color><amount> diamond <text_color>for boosting!"),
            Reward("give <player> emerald <amount>"),
            Reward("lp user <player> parent addtemp vip <amount>d", "<prefix>You have received <highlight_color><amount> days VIP rank<text_color> for boosting!"),
        )
    )


    @ConfigSerializable
    data class Reward(
        val command: String,
        val message: String? = null,
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