package ir.taher7.gametools.config

import org.sayandev.stickynote.bukkit.pluginDirectory
import org.sayandev.stickynote.core.configuration.Config
import org.spongepowered.configurate.objectmapping.ConfigSerializable
import java.io.File

var messageConfig: MessageConfig = MessageConfig.fromConfig() ?: MessageConfig.defaultConfig()

@ConfigSerializable
data class MessageConfig(
    val general: General = General(),
    val error: Error = Error(),
    val vote: Vote = Vote(),
    val boost: Boost = Boost(),
) : Config(pluginDirectory, FILE_NAME) {
    init {
        load()
    }


    @ConfigSerializable
    data class General(
        val prefix: String = "<yellow>[<green>GameTools<yellow>] <text_color>",
        val textColor: String = "<yellow>",
        val hoverColor: String = "<gray>",
        val highlightColor: String = "<green>",
        val coolDown: String = "<prefix><red>You have to wait <time> seconds to use this command again!",
        val reload: String = "<prefix>Reloading configuration..."
    )

    @ConfigSerializable
    data class Error(
        val noPermission: String = "<prefix><red>You don't have permission to do this!",
        val playerNotFound: String = "<prefix><red>Player not found!",
        val disconnectSocket: String = "<prefix><red>Something went wrong! Please try again later.",
    )

    @ConfigSerializable
    data class Vote(
        val vote: String = "<click:open_url:'<url>'><prefix>Click here to vote for <highlight_color><server></click>",
        val alreadyVoted: String = "<prefix><red>You have already voted! and received rewards.",
        val newVote: String = "<prefix><text_color>Thank you for voting and supporting us.",
        val broadcastNewVote: List<String> = listOf(
            "",
            "",
            "<prefix>A new vote has been received from <highlight_color><player> <text_color>.",
            "<prefix><highlight_color><server> <text_color>Game Tools Vote Rank: <highlight_color><server_rank>",
            "<prefix><highlight_color><server> <text_color>Total Votes: <highlight_color><server_votes>",
            "<click:open_url:'<url>'><prefix>Click here to vote for<highlight_color> <server> </click>",
            "",
            "",
        ),
        val autoAnnounce: List<String> = listOf(
            "",
            "",
            "<prefix>Vote for <highlight_color><server> <text_color>and get rewards!",
            "<click:run_command:'/gametools vote'><prefix>Click here to vote for<highlight_color> <server> </click>",
            "",
            "",
        )
    )

    @ConfigSerializable
    data class Boost(
        val newBoost: String = "<prefix>Thank you for <highlight_color><amount> <text_color>boosting and supporting us.",
        val broadcastNewBoost: List<String> = listOf(
            "",
            "",
            "<prefix>A new boost has been received from <highlight_color><player> <text_color>.",
            "<prefix><highlight_color><server> <text_color>Game Tools Boost Rank: <highlight_color><server_rank>",
            "<prefix><highlight_color><server> <text_color>Total Boosts: <highlight_color><server_boosts>",
            "<click:open_url:'<url>'><prefix>Click here to boost <highlight_color><server></click>",
            "",
            "",
        ),
        val autoAnnounce: List<String> = listOf(
            "",
            "",
            "<prefix>Boost <highlight_color><server> <text_color>and get rewards!",
            "<click:open_url:'<url>'><prefix>Click here to boost <highlight_color><server> </click>",
            "",
            "",
        )
    )


    companion object {
        private const val FILE_NAME = "message.yml"
        private val messageConfigFile = File(pluginDirectory, FILE_NAME)

        fun defaultConfig(): MessageConfig {
            return MessageConfig()
        }

        fun fromConfig(): MessageConfig? {
            return fromConfig<MessageConfig>(messageConfigFile)
        }

        fun reload() {
            messageConfig = fromConfig() ?: defaultConfig()
        }

    }


}