package ir.taher7.gametools.websocket.models

import com.google.gson.annotations.Expose

data class ServerData(
    @Expose val name: String,
    @Expose val color: String,
    @Expose val address: String,
) {
}