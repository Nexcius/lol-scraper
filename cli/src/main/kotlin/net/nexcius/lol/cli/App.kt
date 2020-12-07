@file:OptIn(ExperimentalCli::class)
package net.nexcius.lol.cli

import kotlinx.cli.*
import net.nexcius.lol.cli.datasource.DataSource
import net.nexcius.lol.cli.datasource.Opgg
import java.util.*
import java.io.File


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
    val source by argument(ArgType.Choice<Source>(), "source")
    val role by option(ArgType.Choice<Role>(), "role", shortName = "r").required()
    val output by option(ArgType.String, shortName = "o").default("output.csv")
    val include by option(ArgType.String, shortName = "i").default("")
    val exclude by option(ArgType.String, shortName = "e").default("")

    override fun execute() {
        val champs = resolveChampsFromArgs(role, include, exclude)

        println("Fetching data for champions: ${champs.joinToString(", ")}")
        val data = when(source) {
            Source.OPGG -> Opgg.gather(champs, role)
        }

        println("Writing results to file: $output")
        OutputWriter.write(output, data)

        println("Done")
    }
}

class CommandFetchChamps : Subcommand("fetch-champs", "Fetch data for champions") {
    val source by argument(ArgType.Choice<Source>())
    val champFile by argument(ArgType.String)
    val role by option(ArgType.Choice<Role>(), "role", shortName = "r").required()
    val output by option(ArgType.String, shortName = "o").default("output.csv")

    override fun execute() {
        val file = File(champFile)
        val champs = file.useLines{ it.toList() }.map(::resolveChampionName).toSortedSet()

        println("Fetching data for champions: ${champs.joinToString(", ")}")
        val data = when(source) {
            Source.OPGG -> Opgg.gather(champs, role)
        }

        println("Writing results to file: $output")
        OutputWriter.write(output, data)

        println("Done")
    }
}

fun main(args: Array<String>) {
    val argParser = ArgParser("lolscraper")

    val cmdChamps = CommandChamps()
    val cmdFetch = CommandFetch()
    val cmdFetchChamps = CommandFetchChamps()

    argParser.subcommands(cmdChamps, cmdFetch, cmdFetchChamps)
    argParser.parse(args)
}
