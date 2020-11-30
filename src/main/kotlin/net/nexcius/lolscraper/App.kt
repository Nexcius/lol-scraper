package net.nexcius.lolscraper

import it.skrape.core.fetcher.HttpFetcher
import it.skrape.core.htmlDocument
import it.skrape.extract
import it.skrape.selects.DocElement
import it.skrape.skrape
import kotlinx.serialization.Serializable
import java.io.File
import java.text.NumberFormat
import kotlin.system.exitProcess

//val champions = mapOf(
//        523 to "Aphelios",
//        22 to "Ashe",
//        51 to "Caitlyn",
//        119 to "Draven",
//        81 to "Ezreal",
//        202 to "Jhin",
//        222 to "Jinx",
//        145 to "Kaisa",
//        429 to "Kalista",
//        96 to "KogMaw",
//        236 to "Lucian",
//        21 to "MissFortune",
//        360 to "Samira",
//        235 to "Senna",
//        15 to "Sivir",
//        18 to "Tristana",
//        110 to "Varus",
//        67 to "Vayne",
//        498 to "Xayah",
//        157 to "Yasuo",
//)


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

fun opgg() {
    val champions = listOf(
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
            "Xayah",
            "Yasuo",
            "Swain",
            "Cassiopeia"
    )

    val allResults = champions.map{
        Thread.sleep(100)
        it to fetchOPGGStats(it)
    }.toMap().toSortedMap()

    val file = File("opgg.csv")
    file.writeText("")

    file.appendText(",${allResults.keys.joinToString(",")}\n")

    allResults.forEach { (champ, vs) ->
        file.appendText("$champ,")

        val row = allResults.keys.map { vsChamp ->
            vs.getOrElse(vsChamp) {
                allResults[vsChamp]?.get(champ)
                        ?.let { 1.0f - NumberFormat.getPercentInstance().parse(it).toFloat() }
                        ?.toString()
                        ?: ""
            }

//            vs[vsChamp] ?: ""
        }

        file.appendText("${row.joinToString(",")}\n")
    }
}

fun fetchOPGGStats(champion: Champion): Map<Champion, String> {
    println("Fetching stats for $champion")
    var vsList = mapOf<Champion, String>()

    skrape(HttpFetcher) {
        request {
            url = "https://euw.op.gg/champion/$champion/statistics/adc/matchup"
        }

        extract {
            htmlDocument {
                vsList = findFirst(".champion-matchup-champion-list")
                        .findAll(".champion-matchup-champion-list__item")
                        .map { cell ->
                            val container = cell.findFirst(".champion-matchup-list__champion")
                            container.findFirst("span").text to container.findSecond("span").text
                        }.toMap()
            }
        }
    }

    return vsList
}


fun main() {

    opgg()

    println("Done!")
    exitProcess(0)

//    val OUTFILE = "out.json"
//    val champs: List<Champion> = listOf(
//            "Aphelios",
//            "Ashe",
//            "Caitlyn",
//            "Draven",
//            "Ezreal",
//            "Jhin",
//            "Jinx",
//            "Kai'Sa",
//            "Kalista",
//            "Kog'Maw",
//            "Lucian",
//            "Miss Fortune",
//            "Samira",
//            "Senna",
//            "Sivir",
//            "Tristana",
//            "Twitch",
//            "Varus",
//            "Vayne",
//            "Xayah")
//
//
//
//    if (!File(OUTFILE).exists()) {
//        println("File <$OUTFILE> doesn't exist, fetching it again")
//        runBlocking {
//            val results = champs
//                    .map {
//                        delay(100)
//                        scrape(it, Role.ADC, Tier.SILVER)
//                    }
//
//            val json = Json.encodeToString(results)
//            File(OUTFILE).writeText(json)
//        }
//    }
//
//    println("Extracting data to CSV")
//    val input = File(OUTFILE).readText()
//    val stats = Json.decodeFromString<List<Stats>>(input)
//    writeToCsv("winrate.csv", stats) { it.winRateAgainst }
//    writeToCsv("lanediff.csv", stats) { it.laneDiffAtFifteen }
//    writeToCsv("pairing.csv", stats) { it.pairingWinRate }
//
//    println("Done")
}

fun writeToCsv(filename: String, statList: List<Stats>, entries: (Stats) -> Map<String, Number>) {
    val file = File(filename)
    file.writeText("")

    val data = statList.sortedBy { it.champion }.map { it.champion to entries(it) }.toMap()
    val allVs = data.values.flatMap { it.keys }.toSortedSet()
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
