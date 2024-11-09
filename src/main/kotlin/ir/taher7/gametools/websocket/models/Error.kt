package ir.taher7.gametools.websocket.models

import com.google.gson.annotations.Expose

data class Error(
    @Expose val message: String
) {}