package net.nexcius.lolscraper

import it.skrape.core.fetcher.HttpFetcher
import it.skrape.core.htmlDocument
import it.skrape.extract
import it.skrape.selects.DocElement
import it.skrape.skrape
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

typealias Champion = String

enum class Role {
    ADC, MIDDLE, TOP, JUNGLE, SUPPORT
}

enum class Tier {
    IRON, BRONZE, SILVER, GOLD, PLATINUM, DIAMOND, MASTER
}

sealed class Stat<T>(val name: String, val description: String, val stats: Pair<Champion, T>) {
    class WinsLaneAgainst(stats: Pair<Champion, Int>) : Stat<Int>(
            "Wins lane against", "Gold difference at 15 minutes", stats)

    class LoosesLaneAgainst(stats: Pair<Champion, Int>) : Stat<Int>(
            "Wins lane against", "Gold difference at 15 minutes", stats)

}

@Serializable
data class Stats(val champion: Champion,
                 val laneDiffAtFifteen: Map<Champion, Int>,
                 val pairingWinRate: Map<Champion, Float>,
                 val winRateAgainst: Map<Champion, Float>
)

object Titles {
    private const val WINS_LANE_AGAINST = "wins lane against"
    private const val LOSES_LANE_AGAINST = "loses lane against"
    private const val IS_BEST_WITH = "is best with"
    private const val WINS_AGAINST = "wins more against"
    private const val LOOSES_AGAINST = "loses more against"

    fun isLaneDiff(title: String) = title.contains(WINS_LANE_AGAINST) || title.contains(LOSES_LANE_AGAINST)
    fun isPairing(title: String) = title.contains(IS_BEST_WITH)
    fun isWinRate(title: String) = title.contains(WINS_AGAINST) || title.contains(LOOSES_AGAINST)
}

fun main() {
    val OUTFILE = "out.json"
    val champs: List<Champion> = listOf(
            "Aphelios",
            "Ashe",
            "Caitlyn",
            "Draven",
            "Ezreal",
            "Jhin",
            "Jinx",
            "Kai'Sa",
            "Kalista",
            "Kog'Maw",
            "Lucian",
            "Miss Fortune",
            "Samira",
            "Senna",
            "Sivir",
            "Tristana",
            "Twitch",
            "Varus",
            "Vayne",
            "Xayah")



    if (!File(OUTFILE).exists()) {
        println("File <$OUTFILE> doesn't exist, fetching it again")
        runBlocking {
            val results = champs
                    .map {
                        delay(100)
                        scrape(it, Role.ADC, Tier.SILVER)
                    }

            val json = Json.encodeToString(results)
            File(OUTFILE).writeText(json)
        }
    }

    println("Extracting data to CSV")
    val input = File(OUTFILE).readText()
    val stats = Json.decodeFromString<List<Stats>>(input)
    writeToCsv("winrate.csv", stats) { it.winRateAgainst }
    writeToCsv("lanediff.csv", stats) { it.laneDiffAtFifteen }
    writeToCsv("pairing.csv", stats) { it.pairingWinRate }

    println("Done")
}

fun writeToCsv(filename: String, statList: List<Stats>, entries: (Stats) -> Map<String, Number>) {
    val file = File(filename)
    file.writeText("")

    val data = statList.sortedBy { it.champion }.map { it.champion to entries(it) }.toMap()
    val allVs = data.values.flatMap{it.keys}.toSortedSet()
    file.appendText(",${allVs.joinToString(",")}\n")

    data.forEach { (champ, stats) ->
        file.appendText("$champ,")

        val row = allVs.map { vsChamp ->
            stats[vsChamp]?.toString() ?: ""
        }

        file.appendText("${row.joinToString(",")}\n")
    }
}

fun getMatchup(row: DocElement): Pair<String, String> {
    val vs = row.findFirst(".name").text
    val rate = row.findFirst("progressbar").attribute("data-value")
    return Pair(vs, rate)
}

fun scrape(champ: Champion, role: Role, tier: Tier): Stats {
    val champNiceName = champ.toLowerCase().replace("[^a-z]".toRegex(), "")

    val laneDiffAtFifteen = mutableMapOf<Champion, Int>()
    val pairingWinRate = mutableMapOf<Champion, Float>()
    val winRateAgainst = mutableMapOf<Champion, Float>()

    skrape(HttpFetcher) {
        request {
            url = "https://www.leagueofgraphs.com/champions/counters/$champNiceName/${role.name.toLowerCase()}/${tier.name.toLowerCase()}"
        }

        extract {
            htmlDocument {
                findAll(".box:has(h3)").forEach { dataList ->
                    val title = dataList.findFirst("h3").text
                    val entries = kotlin.runCatching { dataList.findAll("tr:has(span.name)") }.getOrDefault(listOf()).map(::getMatchup)

                    when {
                        Titles.isLaneDiff(title) -> {
                            laneDiffAtFifteen.putAll(entries.map { Pair(it.first, it.second.toInt()) })
                        }

                        Titles.isPairing(title) -> {
                            pairingWinRate.putAll(entries.map { Pair(it.first, it.second.toFloat()) })
                        }

                        Titles.isWinRate(title) -> {
                            winRateAgainst.putAll(entries.map { Pair(it.first, it.second.toFloat()) })
                        }
                    }
                }
            }
        }
    }

    return Stats(champ, laneDiffAtFifteen, pairingWinRate, winRateAgainst)
}
