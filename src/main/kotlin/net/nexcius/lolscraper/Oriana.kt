package net.nexcius.lolscraper

import com.beust.klaxon.Klaxon
import com.merakianalytics.orianna.Orianna
import com.merakianalytics.orianna.types.common.Queue
import com.merakianalytics.orianna.types.common.Region
import com.merakianalytics.orianna.types.common.Tier
import com.merakianalytics.orianna.types.core.summoner.Summoner
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.get
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import org.joda.time.DateTime
import java.net.URL

data class DataSummoner(val name: String, val id: String, val accountId: String) {
    companion object {
        fun create(summoner: Summoner) = DataSummoner(summoner.name, summoner.id, summoner.accountId)
    }
}

fun main() {
    Orianna.setRiotAPIKey("RGAPI-229bc701-1a47-45b0-9e54-30338c0673a7")
    Orianna.setDefaultRegion(Region.EUROPE_WEST)
    val champions = Orianna.getChampions()
    champions.forEach { println(it.id.toString() + " to " + it.name + ",\n") }
//    println(champions)

//    val summoner: Summoner = Orianna.summonerNamed("Nexcius").get()

//    val json = Json.encodeToString(summoner)Dat
//
//    val a = DataSummoner.create(summoner)
//    val json = Klaxon().toJsonString(a)
//    println(a.name)
//
//    println(json)
//
//    val sum2 = Klaxon().parse<DataSummoner>(json)!!
//    println(sum2.name)

//    Orianna.summonerWithId("w1lYm_D4m8E-nCKzxCxXWze5tTecOYO88PXEXMjwklHMIRWN").get()

//    val league = Orianna.leaguePositionsForSummoner(summoner).get()
//
//    league.first().tier
//    println(league)
//
//    Orianna.matchHistoryForSummoner(summoner)
//            .withStartTime(DateTime.now().minusDays(5))
//            .withQueues(Queue.RANKED_SOLO)
//            .get()
//
////    println()
//
//    val client = HttpClient()
//
//
//    runBlocking {
//        val seasons = client.get<String>("http://static.developer.riotgames.com/docs/lol/seasons.json")
//
//        val x = Json.decodeFromString<JsonArray>(seasons)
////        val queues = client.get<String>("http://static.developer.riotgames.com/docs/lol/queues.json")
////        println(seasons)
//
//        val a = x.map {
//            val o = it.jsonObject
//            o["id"]!!.jsonPrimitive.int to o["season"]!!.jsonPrimitive.toString()
//        }.toMap()
//
//        println(a)
//    }


//    val match = Orianna.matchWithId(4905989875).get()
//    match.se
//    println(match)
}