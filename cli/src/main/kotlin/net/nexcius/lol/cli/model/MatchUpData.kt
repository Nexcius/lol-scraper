package net.nexcius.lol.cli.model

import net.nexcius.lol.cli.ChampionName
import net.nexcius.lol.cli.Role

data class MatchUpData(val championName: ChampionName, val role: Role, val matchUps: Map<ChampionName, Float>) {
    fun goodMatchUps() = if(matchUps.isEmpty()) 0.0 else matchUps.count { it.value > 0.5 }.toFloat() / matchUps.size.toFloat()
    fun matchUpAverage() = if(matchUps.isEmpty()) 0.0 else matchUps.values.sum() / matchUps.size

    companion object {
        fun List<MatchUpData>.fillMissing(): List<MatchUpData> {
            val allChamps = this.map { it.championName }

            return this.map { element ->
                val filledMatchUps = allChamps.map { vsChamp ->
                    vsChamp to element.matchUps.getOrElse(vsChamp) {
                        if(element.championName != vsChamp) {
                            println("Attempting to fill missing match-up for <${element.championName}> vs <$vsChamp>")
                        }
                        this.find { it.championName == vsChamp }?.matchUps?.get(element.championName)?.let { 1.0f - it } ?: 0.0f
                    }
                }.toMap()

                element.copy(matchUps = filledMatchUps)
            }
        }
    }
}