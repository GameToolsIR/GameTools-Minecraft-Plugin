package ir.taher7.gametools.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder

object GsonUtils {
    private val builder = GsonBuilder().excludeFieldsWithoutExposeAnnotation()
    val gson = builder.create()


    fun <T> Gson.fromJsonOrNull(json: String, key: Class<T>): T? {
        return try {
            this.fromJson(json, key)
        } catch (e: Exception) {
            null
        }
    }

}