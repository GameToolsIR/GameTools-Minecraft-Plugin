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
        val prefix: String = "<gradient:#1363DF:#47B5FF>GameTools</gradient> <#EAEAEA><bold>|</bold> <text_color>",
        val textColor: String = "<gradient:#47B5FF:#1363DF>",
        val hoverColor: String = "<#DF6619>",
        val highlightColor: String = "<#DFF6FF>",
        val header: String = "<st><gradient:#47B5FF:#1363DF>                     </gradient></st> <bold><gradient:#1363DF:#47B5FF>GameTools</gradient></bold> <st><gradient:#47B5FF:#1363DF>                      </st>",
        val footer: String = "<st><gradient:#47B5FF:#1363DF><st>                                                             ",
        val coolDown: String = "<prefix>Lotfan <highlight_color><time> <text_color>Sanie Sabr Konid va Dobare Emtehan Konid.",
        val reload: String = "<prefix>Tamami Config ha ba Movafaghiat Reload Shodand.",
    )

    @ConfigSerializable
    data class Error(
        val noPermission: String = "<prefix>Shoma Dastresi kafi Baraye Anjam in kar ra Nadarid.",
        val playerNotFound: String = "<prefix>Playeri ba in Name Yaft Nashod.",
        val disconnectSocket: String = "<prefix>Moshkeli Pish Amade ast Lotfan Badan Emtehan Konid.",
    )

    @ConfigSerializable
    data class Vote(
        val vote: String = "<click:open_url:'<url>'><prefix>Baraye Ray Dadan be Server <server_color><server> <text_color> Click konid ↗</click>",
        val alreadyVoted: String = "<prefix>Shoma Ghablan Ray Dade'id.",
        val newVote: String = "<prefix><text_color>Ray Shoma ba Movafaghiat Sabt Shod, Mamnon Babate Ray Dadan Va Hemayat az Ma.",
        val giveReward: String = "<prefix>Javayez Ray ba Movafaghiat be <highlight_color><player> <text_color>Dade shod!",
        val broadcastNewVote: List<String> = listOf(
            "",
            "<header>",
            "",
            "<prefix><highlight_color><player> <text_color>Be Server <server_color><server> <text_color>Ray Dad!",
            "<prefix><server_color><server> <text_color>Vote Rank: <i>#<server_color><server_rank></i>",
            "<prefix><server_color><server> <text_color>Tedad kol Ray ha: <server_color><server_votes>",
            "",
            "<click:run_command:'/gametools vote'><prefix>Baraye Ray Dadan be Server <server_color><server> <text_color>va Daryaft Javayez Click Konid↗</click>",
            "",
            "<footer>",
            "",
        ),
        val autoAnnounce: List<String> = listOf(
            "",
            "<header>",
            "",
            "<prefix>Shoma Mitavanid Ba Ray Dadan be Server <server_color><server> <text_color>Javayez Daryaft Konid!",
            "<click:run_command:'/gametools vote'><prefix>Baraye Ray Dadan be Server<server_color><server> <text_color>Click Konid!</click>",
            "",
            "<footer>",
            "",
        )
    )

    @ConfigSerializable
    data class Boost(
        val newBoost: String = "<prefix><highlight_color><amount> <text_color>Boost ba Movafaghiat Sabt Shod, Mamnon Babate Boost va Hemayat az Ma.",
        val giveReward: String = "<prefix>Javayez <highlight_color><amount> Boost <text_color> ba Movafaghiat be <highlight_color><player> <text_color>Dade shod!",
        val broadcastNewBoost: List<String> = listOf(
            "",
            "<header>",
            "",
            "<prefix>Bazikon <highlight_color><player>, <text_color>Tedad <highlight_color><amount> Boost <text_color>be Server <server_color><server> <text_color>Ezafe kard!",
            "<prefix><server_color><server> <text_color>Boost Rank: <i>#<server_color><server_rank></i>",
            "<prefix><server_color><server> <text_color>Tedad kol Boost ha: <server_color><server_boosts>",
            "",
            "<click:open_url:'<url>'><prefix>Braye Boost Kardan Server <server_color><server> <text_color>va Daryaft Javayez Click Konid ↗</click>",
            "",
            "<footer>",
            "",
        ),
        val autoAnnounce: List<String> = listOf(
            "",
            "<header>",
            "",
            "<prefix>Boost <server_color><server> <text_color>and get rewards!",
            "<click:open_url:'<url>'><prefix>Click here to boost <server_color><server> <text_color>↗</click>",
            "",
            "<footer>",
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