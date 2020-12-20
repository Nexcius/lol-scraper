@file:OptIn(ExperimentalCli::class)
package net.nexcius.lol.cli

import kotlinx.cli.*
import net.nexcius.lol.cli.datasource.Opgg
import net.nexcius.lol.cli.model.MatchUpData.Companion.fillMissing
import kotlin.system.exitProcess


typealias ChampionName = String

enum class Source {
    OPGG;
}



enum class Role {
    ADC, TOP, JUNGLE, MIDDLE, SUPPORT
}

class CommandChamps : Subcommand("champs", "List champions in different roles") {
    val role by option(ArgType.Choice<Role>(), "role", shortName = "r").required()
    val include by option(ArgType.String, shortName = "i").default("")
    val exclude by option(ArgType.String, shortName = "e").default("")

    override fun execute() {
        val result = resolveChampsFromArgs(role, include, exclude)
        println("Champions:")
        result.forEach { println("\t$it") }
    }
}

class CommandFetch : Subcommand("fetch", "Fetch data for champions") {
    private enum class FetchVariant {
        ALL, MATCHUPS, BASE;
    }

    private val variant by argument(ArgType.Choice<FetchVariant>(), "variant")
    private val role by option(ArgType.Choice<Role>(), "role", shortName = "r").required()
    private val source by option(ArgType.Choice<Source>(), "source", shortName = "s").default(Source.OPGG)

    private val include by option(ArgType.String, shortName = "i").default("")
    private val exclude by option(ArgType.String, shortName = "e").default("")

    override fun execute() {
        val champions = resolveChampsFromArgs(role, include, exclude)
        println("Fetching data for champions: ${champions.joinToString(", ")}")

        val dataSource = when(source) {
            Source.OPGG -> Opgg
        }

        if (variant == FetchVariant.ALL || variant == FetchVariant.BASE) {
            val baseData = champions.map {
                Thread.sleep(100)
                dataSource.getBaseData(it, role)
            }

            val fileName = "${source.name.toLowerCase()}-basedata.csv"
            println("Writing base data to file: $fileName")
            OutputWriter.writeBaseData(fileName, baseData)
        }

        if (variant == FetchVariant.ALL || variant == FetchVariant.MATCHUPS) {
            val matchUps = champions.map {
                Thread.sleep(100)
                dataSource.getMatchUps(it, role)
            }.fillMissing()

            val fileName = "${source.name.toLowerCase()}-matchups.csv"
            println("Writing match-ups to file: $fileName")
            OutputWriter.writeMatchUpsData(fileName, matchUps)
        }

        println("Done")
    }
}


fun main(args: Array<String>) {
    val argParser = ArgParser("lolscraper")

    val cmdChamps = CommandChamps()
    val cmdFetch = CommandFetch()

    argParser.subcommands(cmdChamps, cmdFetch)
    argParser.parse(args)

    exitProcess(0)
}
