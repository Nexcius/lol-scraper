package net.nexcius.lol.scraper.repository

import com.google.gson.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.nexcius.lol.scraper.serialization.DateTimeSerializer
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.io.File
import java.lang.reflect.Type


@Serializable
data class LastSeenEntry(val key: String, @Serializable(with = DateTimeSerializer::class) val value: DateTime) {
    companion object {
        fun NeverSeen(summonerName: String) = LastSeenEntry(summonerName, DateTime.now().withYear(2000))
    }
}

class DateTimeTypeAdapter : JsonSerializer<DateTime>, JsonDeserializer<DateTime> {
    private val df = DateTimeFormat.forPattern("yyyy/MM/dd HH:ss")

    override fun serialize(
        src: DateTime,
        typeOfSrc: Type,
        context: JsonSerializationContext?): JsonElement {

        return JsonPrimitive(src.toString(df))
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): DateTime {
        val x = df.parseDateTime(json.asString)

        println(x)

        return x
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

class InMemoryDatabase<T>(private val filename: String) {
    val data = mutableListOf<T>()

    fun load() {
        val file = File(filename)

        if (file.exists()) {
            val loadedData = Json.decodeFromString<List<T>>(file.readText())
            data.addAll(loadedData)
        }
    }

    fun save() {
        val file = File(filename)
        if(!file.exists()) {
            file.createNewFile()
        }

        file.writeText(Json.encodeToString(data))
    }
}
