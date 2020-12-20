package net.nexcius.lol.cli.datasource

import it.skrape.core.fetcher.HttpFetcher
import it.skrape.core.htmlDocument
import it.skrape.extract
import it.skrape.skrape
import net.nexcius.lol.cli.ChampionName
import net.nexcius.lol.cli.Role
import net.nexcius.lol.cli.model.BaseData
import net.nexcius.lol.cli.model.MatchUpData
import net.nexcius.lol.cli.model.WinRateByGameLength
import net.nexcius.lol.cli.parsePercentage
import java.text.NumberFormat

object Opgg : DataSource {

    override fun getMatchUps(championName: ChampionName, role: Role): MatchUpData {
        println("Fetching match-ups for $championName")
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

        return MatchUpData(championName, role, vsList)
    }

    override fun getBaseData(championName: ChampionName, role: Role): BaseData {
        return skrape(HttpFetcher) {
            request {
                url = "https://euw.op.gg/champion/$championName/statistics/${role.name.toLowerCase()}"
            }

            return@skrape extract {
                htmlDocument {
                    val sideContent = findFirst(".l-champion-statistics-content__side")
                    val trends = sideContent.findAll(".champion-stats-trend")

                    val winRateContainer =
                        trends.find { it.findFirst(".champion-stats-trend-average").text.endsWith("Win Rate") }
                    val winRate = winRateContainer?.findFirst(".champion-stats-trend-rate")?.text?.parsePercentage()

                    val pickRateContainer =
                        trends.find { it.findFirst(".champion-stats-trend-average").text.endsWith("Pick Rate") }
                    val pickRate = pickRateContainer?.findFirst(".champion-stats-trend-rate")?.text?.parsePercentage()
                    val scripts = sideContent.findAll("script").map { it.toString() }
                    val winRateByLengthScript = scripts.find { it.contains("Trend-GameLengthWinRateGraph") }
                    checkNotNull(winRateByLengthScript)

                    val arrayRegex = Regex("\\[.+?\\]")
                    val winRatePerGameLengthArray =
                        arrayRegex.findAll(winRateByLengthScript).elementAt(1).value.trim('[', ']')
                    val objectRegex = Regex("\\{.+?\\}")
                    val winRatePerGameLengthEntries = objectRegex.findAll(winRatePerGameLengthArray).map { it.value }

                    val yValueExtractor = Regex("\"y\": ?(\\d+\\.\\d+)")
                    val winRatePerGameLengthPercentagesRaw =
                        winRatePerGameLengthEntries.map { yValueExtractor.find(it)?.groupValues?.get(1)?.toFloat() }
                            .toList()
                    val winRatePerGameLengthPercentages =
                        WinRateByGameLength.fromList(winRatePerGameLengthPercentagesRaw)
                    println(winRatePerGameLengthPercentages)

                    BaseData(championName, winRate, pickRate, winRatePerGameLengthPercentages)
                }
            }
        }
    }
}