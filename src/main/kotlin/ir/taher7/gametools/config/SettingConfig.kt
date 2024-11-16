package ir.taher7.gametools.config

import org.sayandev.stickynote.bukkit.pluginDirectory
import org.sayandev.stickynote.core.configuration.Config
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import java.io.File

var settingConfig: SettingConfig = SettingConfig.fromConfig() ?: SettingConfig.defaultConfig()

@ConfigSerializable
data class SettingConfig(
    val vote: Vote = Vote(),
    val boost: Boost = Boost(),
) : Config(pluginDirectory, FILE_NAME) {

    init {
        load()
    }


    @ConfigSerializable
    data class Vote(
        val enable: Boolean = true,
        val broadcastMessage: Boolean = true,
        val onlyBroadcastExistPlayerMessage: Boolean = false,
        val getRewardJustInFirstTime: Boolean = false,
        val autoAnnounce: AutoAnnounce = AutoAnnounce(
            interval = 700
        ),
        val rewards: List<Reward> = listOf(
            Reward(
                "give <player> diamond 1",
                "<prefix>Shoma <highlight_color>1 diamond <text_color>Baraye Ray dadan be Server <server_color><server> <text_color> Daryaft kardid!"
            ),
            Reward("give <player> emerald 1"),
            Reward(
                "lp user <player> parent addtemp vip 3d accumulate",
                "<prefix>Shoma <highlight_color>3 Roz Rank VIP <text_color>Baraye Ray dadan be Server <server_color><server> <text_color> Daryaft kardid!"
            ),
        )
    )


    @ConfigSerializable
    data class Boost(
        val enable: Boolean = true,
        val broadcastMessage: Boolean = true,
        val onlyBroadcastExistPlayerMessage: Boolean = false,
        val autoAnnounce: AutoAnnounce = AutoAnnounce(
            interval = 900
        ),
        val rewards: List<Reward> = listOf(
            Reward(
                "give <player> diamond <amount>",
                "<prefix>Shoma <highlight_color><amount> diamond <text_color>Baraye Boost kardan Server <server_color><server> <text_color>Daryaft kardid!"
            ),
            Reward("give <player> emerald <amount>"),
            Reward(
                "lp user <player> parent addtemp booster <amount>d accumulate",
                "<prefix>Shoma <highlight_color><amount> Roz Rank Booster<text_color> Baraye Boost kardan Server <server_color><server> <text_color>Daryaft kardid!"
            ),
        )
    )


    @ConfigSerializable
    data class Reward(
        val command: String,
        val message: String? = null,
    )

    @ConfigSerializable
    data class AutoAnnounce(
        val enable: Boolean = true,
        val interval: Long = 10,
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