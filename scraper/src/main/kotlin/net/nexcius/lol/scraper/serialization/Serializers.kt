package net.nexcius.lol.scraper.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat


@Serializer(forClass = DateTime::class)
object DateTimeSerializer: KSerializer<DateTime> {
    private val df = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss")

    override fun serialize(encoder: Encoder, value: DateTime) {
        encoder.encodeString(value.toString(df))
    }

    override fun deserialize(decoder: Decoder): DateTime = df.parseDateTime(decoder.decodeString())

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("DateTime", PrimitiveKind.STRING)
}