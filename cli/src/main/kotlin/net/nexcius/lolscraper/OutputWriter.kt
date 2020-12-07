package net.nexcius.lolscraper

import java.io.File

object OutputWriter {
    fun write(path: String, data: Map<ChampionName, Map<ChampionName, Float>>) {
        val file = File(path)
        file.writeText("")

        file.appendText(",${data.keys.joinToString(",")}\n")

        data.forEach { (champ, matchups) ->
            file.appendText("$champ,")
            file.appendText("${matchups.values.joinToString(",")}\n")
        }
    }
}