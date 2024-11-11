package ir.taher7.gametools.websocket.models

import com.google.gson.annotations.Expose

data class NewComment(
    @Expose val discordId: String,
    @Expose val discordDisplayName: String,
    @Expose val content: String,
    @Expose val rating: Int,
    @Expose val serverRating: Float,
    @Expose val serverName: String,
) {}