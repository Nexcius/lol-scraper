package net.nexcius.lol.cli

import net.nexcius.lol.cli.model.BaseData
import net.nexcius.lol.cli.model.MatchUpData
import java.io.File

object OutputWriter {
    fun writeBaseData(path: String, baseData: List<BaseData>) {

    }

    fun writeMatchUpsData(path: String, matchUpData: List<MatchUpData>) {
        val file = File(path)
        file.writeText("")
        file.appendText(",${matchUpData.joinToString(",") { it.championName }}\n")

        matchUpData.forEach { (championName, _, matchUps) ->
            file.appendText("$championName,")
            file.appendText("${matchUps.values.joinToString(",")}\n")
        }
    }
}
