package net.nexcius.lol.cli.datasource

import it.skrape.core.fetcher.HttpFetcher
import it.skrape.core.htmlDocument
import it.skrape.extract
import it.skrape.skrape
import net.nexcius.lol.cli.ChampionName
import net.nexcius.lol.cli.Role
import java.text.NumberFormat

object Opgg : DataSource {

    override fun getChampionData(championName: ChampionName, role: Role): Map<ChampionName, Float> {
        println("Fetching stats for $championName")
        var vsList = mapOf<ChampionName, Float>()

        skrape(HttpFetcher) {
            request {
                url = "https://euw.op.gg/champion/$championName/statistics/${role.name.toLowerCase()}/matchup"
            }

            extract {
                htmlDocument {
                    vsList = findFirst(".champion-matchup-champion-list")
                        .findAll(".champion-matchup-champion-list__item")
                        .map { cell ->
                            val (vsChamp, winPercentage) = cell.findFirst(".champion-matchup-list__champion").let {
                                Pair(it.findFirst("span").text, it.findSecond("span").text)
                            }
                            vsChamp to NumberFormat.getPercentInstance().parse(winPercentage).toFloat()
                        }.toMap()
                }
            }
        }

        return vsList
    }
}