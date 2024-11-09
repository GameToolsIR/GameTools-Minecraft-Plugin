package ir.taher7.gametools.websocket.models

import com.google.gson.annotations.Expose

data class NewVote(
    @Expose val votes: Int,
    @Expose val uuid: String,
) {
}