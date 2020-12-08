package net.nexcius.lol.scraper.repository

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.nexcius.lol.scraper.serialization.DateTimeSerializer
import org.joda.time.DateTime
import java.io.File


@Serializable
data class LastUpdatedEntry(val summonerName: String, @Serializable(with = DateTimeSerializer::class) val lastUpdated: DateTime) {
    companion object {
        fun neverUpdated(summonerName: String) = LastUpdatedEntry(summonerName, DateTime.now().withYear(2000))
    }
}

inline fun <reified T> MutableList<T>.load(filename: String) {
    val file = File(filename)

    if (file.exists()) {
         this.addAll(Json.decodeFromString<List<T>>(file.readText()))
    }
}

inline fun <reified T> MutableList<T>.save(filename: String) {
    val file = File(filename)
    if(!file.exists()) {
        file.createNewFile()
    }

    file.writeText(Json.encodeToString(this))
}
