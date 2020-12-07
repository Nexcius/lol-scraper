package net.nexcius.lol.cli.datasource

import net.nexcius.lol.cli.ChampionName
import net.nexcius.lol.cli.Role

interface DataSource {
    fun gather(champions: Set<ChampionName>, role: Role): Map<ChampionName, Map<ChampionName, Float>> {
        return champions
            .sorted()
            .map {
                Thread.sleep(100)
                it to getChampionData(it, role)
            }
            .toMap()
            .let { fillMissing(it) }
    }

    fun fillMissing(champList: Map<ChampionName, Map<ChampionName, Float>>): Map<ChampionName, Map<ChampionName, Float>> {
        return champList.map { (champ, matchups) ->
            champ to champList.keys.map { vs ->
                vs to matchups.toMutableMap().computeIfAbsent(vs) {
                    if(champ != vs) {
                        println("Attempting to fill missing match-up for <$champ> vs <$vs>")
                    }
                    champList[vs]?.get(champ)?.let { 1.0f - it } ?: 0.0f
                }
            }.toMap()
        }.toMap()
    }

    fun getChampionData(championName: ChampionName, role: Role): Map<ChampionName, Float>
}
