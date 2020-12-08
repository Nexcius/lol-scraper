/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package net.nexcius.lol.scraper

import com.merakianalytics.orianna.Orianna
import com.merakianalytics.orianna.types.common.Region
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.nexcius.lol.scraper.repository.LastUpdatedEntry
import net.nexcius.lol.scraper.repository.load
import net.nexcius.lol.scraper.repository.save
import org.joda.time.DateTime
import org.joda.time.Duration
import java.io.File

const val SEED_USER_NAME = "Nexcius"
const val BATCH_USER_COUNT = 10
val lookback: Duration = Duration.standardDays(2)

fun getRiotKey(): String = File("key.txt").readText()

fun List<LastUpdatedEntry>.usersToRetrieve(): List<String> {
    val outdatedTime = DateTime.now().minus(lookback)

    return this.asSequence()
        .filter {
            it.lastUpdated == null || it.lastUpdated.isBefore(outdatedTime)
        }
        .map { it.summonerName }
        .take(BATCH_USER_COUNT)
        .toList()
}

fun main(args: Array<String>) = runBlocking {
    println(getRiotKey())

    Orianna.setRiotAPIKey(getRiotKey())
    Orianna.setDefaultRegion(Region.EUROPE_WEST)

    val lastSeenDb = mutableListOf<LastUpdatedEntry>().apply { load("lastseendb.json") } // TODO: Re-enable
//    val matchDb = InMemoryDatabase<Match>("matchdb.json").apply { init() }


    val collectionFlow = flow {
        // TODO: Fetch a list of users from DB, otherwise use seed user id
        val usersToUpdate = lastSeenDb.usersToRetrieve()

        if (usersToUpdate.isNotEmpty()) {
            println("Updating users: $usersToUpdate")
            usersToUpdate.forEach { emit(it) }
        } else {
            println("No users found in DB, defaulting to seed user <$SEED_USER_NAME>")
            emit(SEED_USER_NAME)
        }
    }

        .transform {
            emit(Orianna.summonerNamed(it).get())
        }

        .transform { summoner ->
            val matches = Orianna.matchHistoryForSummoner(summoner)
                .withStartTime(DateTime.now().minus(lookback))
                .get().toList()
//            println("Found ${matches.size} matches for ${summoner.name}")

            matches.forEach {
                emit(it)
            }

            lastSeenDb.removeIf { it.summonerName == summoner.name }
            lastSeenDb.add(LastUpdatedEntry(summoner.name, DateTime.now()))
        }
        .onCompletion { if (it == null) println("Completed batch") }


    // TODO: While running, keep fetching data
    // while(running)

    val matches = collectionFlow.toList()
    val seenSummoners = matches.flatMap { match -> match.participants.toList().map { it.summoner.name } }

    val newSummoners = seenSummoners - lastSeenDb.map { it.summonerName }
    lastSeenDb.addAll(newSummoners.map(LastUpdatedEntry::neverUpdated))

    println("Saving last updated DB")
    lastSeenDb.save("lastseendb.json")

    println("Writing matches")
    val matchDump = File("matchdump.json")
    matches.forEach {
        matchDump.appendText("${it.toJSON()}\n")
    }

    // TODO: Store match data

    println("Fetched ${matches.size} matches and saw ${newSummoners.size} new summoners")
}


