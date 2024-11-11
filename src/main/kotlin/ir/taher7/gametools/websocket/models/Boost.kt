package ir.taher7.gametools.websocket.models

import com.google.gson.annotations.Expose

data class NewBoost(
    @Expose val discordId: String,
    @Expose val discordDisplayName: String,
    @Expose val amount: Int,
    @Expose val serverBoosts: Int,
    @Expose val serverBoostRank: Int,
    @Expose val serverName: String,
) {}