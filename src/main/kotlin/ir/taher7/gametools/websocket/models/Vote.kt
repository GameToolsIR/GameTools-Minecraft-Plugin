package ir.taher7.gametools.websocket.models

import com.google.gson.annotations.Expose

data class RequestVote(
    @Expose val token: String,
    @Expose val uuid: String,
) {}

data class NewVote(
    @Expose val player: VotePlayer?,
    @Expose val discordId: String,
    @Expose val discordDisplayName: String,
    @Expose val serverVotes: Int,
    @Expose val serverVoteRank: Int,
) {}

data class VotePlayer(
    @Expose val uuid: String,
    @Expose val username: String,
    @Expose val isFirstTime: Boolean,
) {}